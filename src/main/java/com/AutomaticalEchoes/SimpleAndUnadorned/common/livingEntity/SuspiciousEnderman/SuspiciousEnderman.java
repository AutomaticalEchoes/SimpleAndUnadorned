package com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousEnderman;

import com.AutomaticalEchoes.SimpleAndUnadorned.api.joke.JokeCase;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousEnderman.Goal.DoJoke;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousEnderman.Goal.JokeSelect;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousEnderman.Goal.ScareWhileStared;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousEnderman.Goal.TeleportWhileHasSight;
import com.AutomaticalEchoes.SimpleAndUnadorned.config.ModCommonConfig;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.core.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;


public class SuspiciousEnderman extends Monster {
    public static final ResourceKey<Structure>[] STRUCTURES =new ResourceKey[]{BuiltinStructures.IGLOO,BuiltinStructures.JUNGLE_TEMPLE,BuiltinStructures.WOODLAND_MANSION,
            BuiltinStructures.PILLAGER_OUTPOST,BuiltinStructures.OCEAN_MONUMENT,BuiltinStructures.ANCIENT_CITY};
    private static final DynamicCommandExceptionType ERROR_STRUCTURE_INVALID = new DynamicCommandExceptionType((p_207534_) -> {
        return Component.translatable("commands.locate.structure.invalid", p_207534_);
    });
    private static final EntityDataAccessor<Boolean> DATA_JOKING = SynchedEntityData.defineId(SuspiciousEnderman.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_SCARE = SynchedEntityData.defineId(SuspiciousEnderman.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> JOKING_TARGET_ID = SynchedEntityData.defineId(SuspiciousEnderman.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<ItemStack> CARRIED_ITEM = SynchedEntityData.defineId(SuspiciousEnderman.class ,EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Boolean> SUS_BREAK = SynchedEntityData.defineId(SuspiciousEnderman.class,EntityDataSerializers.BOOLEAN);

//    private static Integer PERSISTENT_SCARE_TIME = 20 * ModCommonConfig.ENDER_SCARE.get();
    private @Nullable LivingEntity JokingTarget;
    private int lastTeleportTime = 0;
    private int clamDown = 0;
    private int lastJokeTime = 0;
    private boolean angry = false;
    private JokeCase<?> joke;
    private DoJoke doJoke ;
    private JokeSelect jokeSelect;
    private final TargetingConditions selector = TargetingConditions
            .forCombat()
            .range(this.getAttributeValue(Attributes.FOLLOW_RANGE));


    public SuspiciousEnderman(EntityType<? extends SuspiciousEnderman> p_32485_, Level p_32486_) {
        super(p_32485_, p_32486_);
        this.clamDown=0;

    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, (double)0.3F)
                .add(Attributes.ATTACK_DAMAGE, 0.5D)
                .add(Attributes.FOLLOW_RANGE, 64.0D);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag p_21484_) {
        super.addAdditionalSaveData(p_21484_);
        p_21484_.putBoolean("sus_break",isSusBreak());
        p_21484_.put("carried_item",this.entityData.get(CARRIED_ITEM).save(new CompoundTag()));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag p_21450_) {
        super.readAdditionalSaveData(p_21450_);
        this.setCarriedItem(ItemStack.of(p_21450_.getCompound("carried_item")));
        this.susBreak(p_21450_.getBoolean("sus_break"));
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_JOKING, false);
        this.entityData.define(DATA_SCARE,false);
        this.entityData.define(CARRIED_ITEM,ItemStack.EMPTY);
        this.entityData.define(JOKING_TARGET_ID,0);
        this.entityData.define(SUS_BREAK,false);
    }

    @Override
    protected void registerGoals() {
        this.doJoke = new DoJoke(this);
        this.jokeSelect = new JokeSelect(this);
        FloatGoal floatGoal = new FloatGoal(this);
        WaterAvoidingRandomStrollGoal waterAvoidingRandomStrollGoal = new WaterAvoidingRandomStrollGoal(this, 1.0D, 0.0F);
        floatGoal.setFlags(EnumSet.of(Goal.Flag.MOVE));
        waterAvoidingRandomStrollGoal.setFlags(EnumSet.of(Goal.Flag.MOVE));
        this.goalSelector.addGoal(0, floatGoal);
        this.goalSelector.addGoal(3, waterAvoidingRandomStrollGoal);
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(5, this.doJoke);
        this.goalSelector.addGoal(6, this.jokeSelect);
        this.goalSelector.addGoal(7, new TeleportWhileHasSight(this));
        this.goalSelector.addGoal(8, new ScareWhileStared(this));
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    @Override
    public double getPassengersRidingOffset() {
       return (double)this.getBbHeight() * 0.15D;
    }

    @Override
    public void positionRider(Entity p_19957_) {
//        super.positionRider(p_19957_);
        if (p_19957_ instanceof LivingEntity mob) {
             mob.yBodyRot = this.yBodyRot +90;
        }
        float f3 = Mth.sin(this.yBodyRot * ((float)Math.PI / 180F));
        float f = Mth.cos(this.yBodyRot * ((float)Math.PI / 180F));
        float f1 = 0.7F ;
        float f2 = - 0.35F;
        p_19957_.setPos(this.getX() - (double)(f1 * f3), this.getY() + this.getPassengersRidingOffset() + p_19957_.getMyRidingOffset() + (double)f2, this.getZ() + (double)(f1 * f));
    }


    @Override
    public void tick() {
        if(this.CarryNothing() && !isAngry() && tickCount > lastJokeTime + 6000 ) angry = this.random.nextInt(3600) < (tickCount - lastJokeTime)/20;
        if(this.isScared()){
            this.clamDown = 20 * ModCommonConfig.ENDER_SCARE.get();;
        }else if (clamDown > 0 ){
            clamDown--;
        }
        super.tick();
    }

    @Override
    protected void actuallyHurt(DamageSource p_21240_, float p_21241_) {
        super.actuallyHurt(p_21240_, p_21241_);
        this.dropCarried();
        this.stopCatching();
        this.teleport();
        this.setAngry(random.nextInt(100) < 1);
    }

    @Override
    public void setTarget(@Nullable LivingEntity p_21544_) {
        super.setTarget( p_21544_);
    }

    public boolean isLookingAtMe(Player p_32535_) {
        ItemStack itemstack = p_32535_.getInventory().armor.get(3);
        if (itemstack.getItem() == Items.DRAGON_HEAD ) {
            return false;
        } else {
            Vec3 vec3 = p_32535_.getViewVector(1.0F).normalize();
            Vec3 vec31 = new Vec3(this.getX() - p_32535_.getX(), this.getEyeY() - p_32535_.getEyeY(), this.getZ() - p_32535_.getZ());
            double d0 = vec31.length();
            vec31 = vec31.normalize();
            double d1 = vec3.dot(vec31);
            return d1 > 1.0D - 0.025D / d0 && p_32535_.hasLineOfSight(this);
        }
    }

    @Override
    protected void tickDeath() {
        super.tickDeath();
        this.dropCarried();
    }

    public boolean isSensitiveToWater() {
        return true;
    }

    @Override
    protected void customServerAiStep() {
        if (this.level.isDay() && this.tickCount >=  this.lastTeleportTime + 600) {
            float f = this.getLightLevelDependentMagicValue();
            if (f > 0.5F && this.level.canSeeSky(this.blockPosition()) && random.nextFloat() * 30.0F < (f - 0.4F) * 2.0F) {
                this.setTarget(null);
                this.teleport();
                this.lastTeleportTime = this.tickCount;
            }
        }
        super.customServerAiStep();
    }

    @Override
    public void aiStep() {
        if (this.level.isClientSide) {
            for(int i = 0; i < 2; ++i) {
                this.level.addParticle(ParticleTypes.PORTAL, this.getRandomX(0.5D), this.getRandomY() - 0.25D, this.getRandomZ(0.5D), (this.random.nextDouble() - 0.5D) * 2.0D, -this.random.nextDouble(), (this.random.nextDouble() - 0.5D) * 2.0D);
            }
        }

        this.jumping = false;
        super.aiStep();
    }

    public boolean teleport() {
        if (!this.level.isClientSide() && this.isAlive()) {
            double d0 = this.getX() + (this.random.nextDouble() - 0.5D) * 64.0D;
            double d1 = this.getY() + (double)(this.random.nextInt(64) - 32);
            double d2 = this.getZ() + (this.random.nextDouble() - 0.5D) * 64.0D;
            return this.teleport(d0, d1, d2);
        } else {
            return false;
        }
    }

    public boolean teleportTowards(Entity p_32501_) {
        Vec3 vec3 = new Vec3(this.getX() - p_32501_.getX(), this.getY(0.5D) - p_32501_.getEyeY(), this.getZ() - p_32501_.getZ());
        vec3 = vec3.normalize();
        double d0 = 16.0D;
        double d1 = this.getX() + this.random.nextDouble() * 8.0D - vec3.x * 16.0D;
        double d2 = this.getY() + (double)(this.random.nextInt(16) - 8) - vec3.y * 16.0D;
        double d3 = this.getZ() + this.random.nextDouble() * 8.0D - vec3.z * 16.0D;
        return this.teleport(d1, d2, d3);
    }

    public void teleportAim(Vec3 vec3,@Nullable Integer range){
        double x = randomPos(vec3.x , range);
        double y = randomPos(vec3.y , range);
        double z = randomPos(vec3.z , range);
        this.setPos(x,y,z);
        if(this.position().distanceTo(vec3) <2) {
            this.moveTo(vec3);
        }else {
            this.teleportAim(vec3,2);
        }
    }

    private boolean teleport(double p_32544_, double p_32545_, double p_32546_) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(p_32544_, p_32545_, p_32546_);

        while(blockpos$mutableblockpos.getY() > this.level.getMinBuildHeight() && !this.level.getBlockState(blockpos$mutableblockpos).getMaterial().blocksMotion()) {
            blockpos$mutableblockpos.move(Direction.DOWN);
        }

        BlockState blockstate = this.level.getBlockState(blockpos$mutableblockpos);
        boolean flag = blockstate.getMaterial().blocksMotion();
        boolean flag1 = blockstate.getFluidState().is(FluidTags.WATER);
        if (flag && !flag1) {
            net.minecraftforge.event.entity.EntityTeleportEvent.EnderEntity event = net.minecraftforge.event.ForgeEventFactory.onEnderTeleport(this, p_32544_, p_32545_, p_32546_);
            if (event.isCanceled()) return false;
            Vec3 vec3 = this.position();
            boolean flag2 = this.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true);
            if (flag2) {
                this.level.gameEvent(GameEvent.TELEPORT, vec3, GameEvent.Context.of(this));
                if (!this.isSilent()) {
                    this.level.playSound((Player)null, this.xo, this.yo, this.zo, SoundEvents.ENDERMAN_TELEPORT, this.getSoundSource(), 1.0F, 1.0F);
                    this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                }
            }

            return flag2;
        } else {
            return false;
        }
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENDERMAN_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_32527_) {
        return SoundEvents.ENDERMAN_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENDERMAN_DEATH;
    }

    //==================================================================================================

    public void setJokingTarget(@Nullable LivingEntity livingEntity){
        this.JokingTarget=livingEntity;
        if(JokingTarget!=null) {
            this.entityData.set(JOKING_TARGET_ID,JokingTarget.getId());
            this.getEntityData().set(DATA_JOKING,true);
        }
    }

    public void dropCarried(){
        if(!isCarryItemEmpty()) {
            ItemStack itemStack = this.entityData.get(CARRIED_ITEM);
            ItemEntity itemEntity = new ItemEntity(level, this.getX(), this.getEyeY() - 0.4F, this.getZ(), itemStack);
            this.level.addFreshEntity(itemEntity);
            this.setCarriedItem(ItemStack.EMPTY);
        }
    }

    public void StartJoke(){
        if(SelectTarget()) {
            this.doJoke.start();
            return;
        }
        reset();
    }


    public boolean SelectTarget(){
        selector.selector(this.getJoke().TargetSelector());
        List<LivingEntity> entities = level.getNearbyEntities(LivingEntity.class,selector,this,getBoundingBox().inflate(20.0));
        int size = entities.size();
        if(size >0){
            int i = random.nextInt(size);
            LivingEntity entity = entities.get(i);
            setJokingTarget(entity);
            return true;
        }
        return false;
    }


    public @Nullable BlockPos.MutableBlockPos PreparePos(ServerLevel serverLevel, Integer structureId){
        ResourceKey<Structure> structure = STRUCTURES[structureId - 1];
        Optional<Holder<Structure>> holder = serverLevel.registryAccess().registryOrThrow(Registry.STRUCTURE_REGISTRY).getHolder(structure);
        HolderSet.Direct<Structure> direct = HolderSet.direct(holder.get());
        BlockPos blockpos = serverLevel.getChunkSource().getGenerator().findNearestMapStructure(serverLevel,direct, this.blockPosition(), 100, false).getFirst();
        return blockpos !=null? ValidationPos(blockpos , structure == BuiltinStructures.ANCIENT_CITY ? 0 : 155) : null;
    }

    private BlockPos.MutableBlockPos ValidationPos(BlockPos blockpos,int startY){
        BlockPos.MutableBlockPos blockpos$mutableblockpos = blockpos.mutable();
        blockpos$mutableblockpos.setY(startY);
        boolean isAirOrWater = false;
        boolean isBlockOrWater = this.level.getBlockState(blockpos$mutableblockpos).getMaterial().blocksMotion() || this.level.getFluidState(blockpos$mutableblockpos).is(Fluids.WATER);
        while(blockpos$mutableblockpos.getY() > this.level.getMinBuildHeight() &&( !isBlockOrWater || !isAirOrWater)) {
            isAirOrWater = this.level.getBlockState(blockpos$mutableblockpos).isAir()|| this.level.getFluidState(blockpos$mutableblockpos).is(FluidTags.WATER);
            blockpos$mutableblockpos.move(Direction.DOWN);
            isBlockOrWater = this.level.getBlockState(blockpos$mutableblockpos).getMaterial().blocksMotion() || this.level.getFluidState(blockpos$mutableblockpos).is(FluidTags.WATER);
        }
        return blockpos$mutableblockpos;
    }

    public void startJokeWith(LivingEntity entity, @Nullable Integer structuredDataId){
        this.setJokingTarget(entity);
        this.joke = jokeSelect.ANGRY.WithStructure(structuredDataId);
        this.doJoke.start();
    }

    public void reset(){
        this.entityData.set(JOKING_TARGET_ID,0);
        this.entityData.set(DATA_JOKING,false);
        this.clamDown = 20 * ModCommonConfig.ENDER_SCARE.get();;
        this.jokeComp();
    }

    public void stopCatching(){
        for(Entity entity : getPassengers()){
            entity.stopRiding();
        }
    }

    public void jokeComp(){
        this.teleport();
        this.setLastJokeTime();
        this.angry =false;
    }

    private double randomPos(double pos,@Nullable Integer range){
        return  range == null? pos + this.random.nextInt(): pos + this.random.nextInt(2 * range) - range;
    }

    public void setCarriedItem(ItemStack item){
        this.entityData.set(CARRIED_ITEM,item);
    }

    public ItemStack getCarriedItem(){
        return this.entityData.get(CARRIED_ITEM);
    }

    public boolean isCarryItemEmpty(){
        return getCarriedItem().isEmpty();
    }

    public LivingEntity getJokingTarget() {
        if(this.level.isClientSide && this.level.getEntity(this.entityData.get(JOKING_TARGET_ID)) instanceof  LivingEntity livingEntity) return livingEntity ;
        return JokingTarget;
    }

    public void setLastJokeTime() {
        this.lastJokeTime = this.tickCount;
    }

    public int getLastJokeTime() {
        return lastJokeTime;
    }

    public boolean isAngry() {
        return angry;
    }

    public void setAngry(boolean angry) {
        this.angry = angry;
    }

    public void scareBeingStaredAt() {
        this.entityData.set(DATA_SCARE, true);
    }

    public void beingClown(){
        this.entityData.set(DATA_SCARE,false);
    }

    public boolean isClown(){
        return !isScared() && this.clamDown <=0;
    }

    public boolean isScared(){
        return this.getEntityData().get(DATA_SCARE);
    }

    public boolean isJoking(){
        return this.getEntityData().get(DATA_JOKING);
    }

    public boolean isSusBreak(){
        return this.getEntityData().get(SUS_BREAK);
    }

    public void susBreak(boolean b){
        this.entityData.set(SUS_BREAK,b);
    }

    public boolean CarryNothing(){
        return isCarryItemEmpty() && this.getPassengers().isEmpty();
    }

    public void setJoke(JokeCase<?> joke) {
        this.joke = joke;
    }

    public JokeCase<?> getJoke() {
        return joke;
    }
}
