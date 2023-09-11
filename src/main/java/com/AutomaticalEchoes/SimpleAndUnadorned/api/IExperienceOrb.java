package com.AutomaticalEchoes.SimpleAndUnadorned.api;

import com.AutomaticalEchoes.SimpleAndUnadorned.register.EntityRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddExperienceOrbPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.*;
import java.util.function.Predicate;

public class IExperienceOrb extends ExperienceOrb {
    private static final int LIFETIME = 6000;
    private static final int ENTITY_SCAN_PERIOD = 20;
    private static final int MAX_FOLLOW_DIST = 8;
    private static final int ORB_GROUPS_PER_AREA = 40;
    private static final double ORB_MERGE_DISTANCE = 0.5D;
    private int age;
    private int health = 5;
    private final Set<EntityType<? extends Entity>> entityTypes = new HashSet<>();
    private final Predicate<Entity> PREDICATE = entity -> entityTypes.contains(entity.getType());
    public int value;
    private int count = 1;
    private Entity followingEntity;

    public static IExperienceOrb Create(EntityType<? extends IExperienceOrb> p_20773_, Level p_20774_){
        return new IExperienceOrb(EntityRegister.I_EXPERIENCE_ORB.get(), p_20774_);
    }

    public IExperienceOrb(EntityType<? extends IExperienceOrb> p_20773_, Level p_20774_) {
        super(p_20773_, p_20774_);
        this.entityTypes.add(EntityType.PLAYER);
    }

    public IExperienceOrb(Level p_20776_, double p_20777_, double p_20778_, double p_20779_, int p_20780_) {
        this(EntityRegister.I_EXPERIENCE_ORB.get(), p_20776_);
        this.setPos(p_20777_, p_20778_, p_20779_);
        this.setYRot((float)(this.random.nextDouble() * 360.0D));
        this.setDeltaMovement((this.random.nextDouble() * (double)0.2F - (double)0.1F) * 2.0D, this.random.nextDouble() * 0.2D * 2.0D, (this.random.nextDouble() * (double)0.2F - (double)0.1F) * 2.0D);
        this.value = p_20780_;
        this.entityTypes.add(EntityType.PLAYER);
    }

    public <T extends Entity & HasExperience> IExperienceOrb FollowEntityType(List<EntityType<T>> entityTypes ){
        this.entityTypes.addAll(entityTypes);
        return this;
    }

    public void tick() {
        super.baseTick();
        this.xo = this.getX();
        this.yo = this.getY();
        this.zo = this.getZ();
        if (this.isEyeInFluid(FluidTags.WATER)) {
            this.setUnderwaterMovement();
        } else if (!this.isNoGravity()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.03D, 0.0D));
        }

        if (this.level.getFluidState(this.blockPosition()).is(FluidTags.LAVA)) {
            this.setDeltaMovement((this.random.nextFloat() - this.random.nextFloat()) * 0.2F, (double)0.2F, (double)((this.random.nextFloat() - this.random.nextFloat()) * 0.2F));
        }

        if (!this.level.noCollision(this.getBoundingBox())) {
            this.moveTowardsClosestSpace(this.getX(), (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 2.0D, this.getZ());
        }

        if (this.level instanceof ServerLevel && this.tickCount % 20 == 1) {
            this.scanForEntities();
        }

        if (this.followingEntity != null && (this.followingEntity.isSpectator() || !this.followingEntity.isAlive())) {
            this.followingEntity = null;
        }

        if (this.followingEntity != null) {
            Vec3 vec3 = new Vec3(this.followingEntity.getX() - this.getX(), this.followingEntity.getY() + (double)this.followingEntity.getEyeHeight() / 2.0D - this.getY(), this.followingEntity.getZ() - this.getZ());
            double d0 = vec3.lengthSqr();
            if (d0 < 64.0D) {
                double d1 = 1.0D - Math.sqrt(d0) / 8.0D;
                this.setDeltaMovement(this.getDeltaMovement().add(vec3.normalize().scale(d1 * d1 * 0.1D)));
            }
            if(Math.sqrt(d0) < followingEntity.getBbWidth()){
                this.entityTouch(followingEntity);
            }
        }

        this.move(MoverType.SELF, this.getDeltaMovement());
        float f = 0.98F;
        if (this.onGround) {
            BlockPos pos =new BlockPos(this.getX(), this.getY() - 1.0D, this.getZ());
            f = this.level.getBlockState(pos).getFriction(this.level, pos, this) * 0.98F;
        }

        this.setDeltaMovement(this.getDeltaMovement().multiply((double)f, 0.98D, (double)f));
        if (this.onGround) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, -0.9D, 1.0D));
        }

        ++this.age;
        if (this.age >= 6000) {
            this.discard();
        }

    }

    private void scanForEntities() {
        if (this.followingEntity == null || this.followingEntity.distanceToSqr(this) > 64.0D) {
            List<Entity> entities = this.level.getEntities(this, new AABB(position().add(-6, -6, -6), position().add(6, 6, 6)));
            Optional<Entity> first = entities.stream().filter(PREDICATE).min(Comparator.comparingDouble(this::distanceToSqr));
            first.ifPresent(entity -> this.followingEntity = entity);
        }

        if (this.level instanceof ServerLevel) {
            for(IExperienceOrb experienceorb : this.level.getEntities(EntityTypeTest.forClass(IExperienceOrb.class), this.getBoundingBox().inflate(0.5D), this::canMerge)) {
                this.merge(experienceorb);
            }
        }
    }


    public static void Award(ServerLevel level , BlockPos pos, int orbNum){
        Award(level ,pos, orbNum, List.of());
    }

    public static <T extends Entity & HasExperience> void Award(ServerLevel level , BlockPos pos, int orbNum, List<EntityType<T>> entityTypes){
        Award(level,new Vec3(pos.getX(), pos.getY(), pos.getZ()).add(0.5,0.5,0.5), orbNum, entityTypes);
    }

    public static <T extends Entity & HasExperience> void Award(ServerLevel p_147083_, Vec3 p_147084_, int p_147085_, List<EntityType<T>> entityTypeList) {
        while(p_147085_ > 0) {
            int i = getExperienceValue(p_147085_);
            p_147085_ -= i;
            if (!tryMergeToExisting(p_147083_, p_147084_, i,entityTypeList)) {
                p_147083_.addFreshEntity(new IExperienceOrb(p_147083_, p_147084_.x(), p_147084_.y(), p_147084_.z(), i).FollowEntityType(entityTypeList));
            }
        }
    }

    private static <T extends Entity & HasExperience> boolean tryMergeToExisting(ServerLevel p_147097_, Vec3 p_147098_, int p_147099_ ,List<EntityType<T>> entityTypeList) {
        AABB aabb = AABB.ofSize(p_147098_, 1.0D, 1.0D, 1.0D);
        int i = p_147097_.getRandom().nextInt(40);
        List<IExperienceOrb> list = p_147097_.getEntities(EntityTypeTest.forClass(IExperienceOrb.class), aabb, (p_147081_) -> canMerge(p_147081_, i, p_147099_));
        if (!list.isEmpty()) {
            IExperienceOrb experienceorb = list.get(0);
            ++experienceorb.count;
            experienceorb.age = 0;
            experienceorb.FollowEntityType(entityTypeList);
            return true;
        } else {
            return false;
        }
    }

    private boolean canMerge(IExperienceOrb p_147087_) {
        return p_147087_ != this && canMerge(p_147087_, this.getId(), this.value);
    }

    private static boolean canMerge(IExperienceOrb p_147089_, int p_147090_, int p_147091_) {
        return !p_147089_.isRemoved() && (p_147089_.getId() - p_147090_) % 40 == 0 && p_147089_.value == p_147091_;
    }

    private void merge(IExperienceOrb p_147101_) {
        this.count += p_147101_.count;
        this.age = Math.min(this.age, p_147101_.age);
        p_147101_.discard();
    }

    private void setUnderwaterMovement() {
        Vec3 vec3 = this.getDeltaMovement();
        this.setDeltaMovement(vec3.x * (double)0.99F, Math.min(vec3.y + (double)5.0E-4F, (double)0.06F), vec3.z * (double)0.99F);
    }

    protected void doWaterSplashEffect() {
    }

    public boolean hurt(DamageSource p_20785_, float p_20786_) {
        if (this.level.isClientSide || this.isRemoved()) return false; //Forge: Fixes MC-53850
        if (this.isInvulnerableTo(p_20785_)) {
            return false;
        } else if (this.level.isClientSide) {
            return true;
        } else {
            this.markHurt();
            this.health = (int)((float)this.health - p_20786_);
            if (this.health <= 0) {
                this.discard();
            }

            return true;
        }
    }

    public void addAdditionalSaveData(CompoundTag p_20796_) {
        p_20796_.putShort("Health", (short)this.health);
        p_20796_.putShort("Age", (short)this.age);
        p_20796_.putShort("Value", (short)this.value);
        p_20796_.putInt("Count", this.count);
    }

    public void readAdditionalSaveData(CompoundTag p_20788_) {
        this.health = p_20788_.getShort("Health");
        this.age = p_20788_.getShort("Age");
        this.value = p_20788_.getShort("Value");
        this.count = Math.max(p_20788_.getInt("Count"), 1);
    }

    public void entityTouch(Entity entity){
        if(!(entity instanceof Player) && this.entityTypes.contains(entity.getType()) && entity instanceof HasExperience experienceEntity){
            experienceEntity.addExperience(this.value);
            this.discard();
        }
    }

    public void playerTouch(Player p_20792_) {
        if (!this.level.isClientSide) {
            if (p_20792_.takeXpDelay == 0) {
                if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.player.PlayerXpEvent.PickupXp(p_20792_, this))) return;
                p_20792_.takeXpDelay = 2;
                p_20792_.take(this, 1);
                int i = this.repairPlayerItems(p_20792_, this.value);
                if (i > 0) {
                    p_20792_.giveExperiencePoints(i);
                }

                --this.count;
                if (this.count == 0) {
                    this.discard();
                }
            }

        }
    }

    private int repairPlayerItems(Player p_147093_, int p_147094_) {
        Map.Entry<EquipmentSlot, ItemStack> entry = EnchantmentHelper.getRandomItemWith(Enchantments.MENDING, p_147093_, ItemStack::isDamaged);
        if (entry != null) {
            ItemStack itemstack = entry.getValue();
            int i = Math.min((int) (this.value * itemstack.getXpRepairRatio()), itemstack.getDamageValue());
            itemstack.setDamageValue(itemstack.getDamageValue() - i);
            int j = p_147094_ - this.durabilityToXp(i);
            return j > 0 ? this.repairPlayerItems(p_147093_, j) : 0;
        } else {
            return p_147094_;
        }
    }

    private int durabilityToXp(int p_20794_) {
        return p_20794_ / 2;
    }

    private int xpToDurability(int p_20799_) {
        return p_20799_ * 2;
    }

    public int getValue() {
        return this.value;
    }

    public int getIcon() {
        if (this.value >= 2477) {
            return 10;
        } else if (this.value >= 1237) {
            return 9;
        } else if (this.value >= 617) {
            return 8;
        } else if (this.value >= 307) {
            return 7;
        } else if (this.value >= 149) {
            return 6;
        } else if (this.value >= 73) {
            return 5;
        } else if (this.value >= 37) {
            return 4;
        } else if (this.value >= 17) {
            return 3;
        } else if (this.value >= 7) {
            return 2;
        } else {
            return this.value >= 3 ? 1 : 0;
        }
    }

    public static int getExperienceValue(int p_20783_) {
        if (p_20783_ >= 2477) {
            return 2477;
        } else if (p_20783_ >= 1237) {
            return 1237;
        } else if (p_20783_ >= 617) {
            return 617;
        } else if (p_20783_ >= 307) {
            return 307;
        } else if (p_20783_ >= 149) {
            return 149;
        } else if (p_20783_ >= 73) {
            return 73;
        } else if (p_20783_ >= 37) {
            return 37;
        } else if (p_20783_ >= 17) {
            return 17;
        } else if (p_20783_ >= 7) {
            return 7;
        } else {
            return p_20783_ >= 3 ? 3 : 1;
        }
    }

    public boolean isAttackable() {
        return false;
    }

    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddExperienceOrbPacket(this);
    }

    public SoundSource getSoundSource() {
        return SoundSource.AMBIENT;
    }
}
