package com.example.gamblerig.mixin;

import com.example.gamblerig.GambleRig;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DispenserBlockEntity.class)
public abstract class DispenserBlockEntityMixin {
    // Vanilla picks a random non-empty slot here; override it for a rigged player nearby.
    @Inject(method = "chooseNonEmptySlot", at = @At("HEAD"), cancellable = true)
    private void gamblerig$rig(Random random, CallbackInfoReturnable<Integer> cir) {
        DispenserBlockEntity self = (DispenserBlockEntity) (Object) this;
        World world = self.getWorld();
        if (!(world instanceof ServerWorld)) return;

        // Only players who passed the allowlist check can ever have enabled=true
        PlayerEntity p = world.getClosestPlayer(
            self.getPos().getX() + 0.5, self.getPos().getY() + 0.5, self.getPos().getZ() + 0.5,
            8.0, e -> GambleRig.state(e.getUuid()).enabled);
        if (p == null) return;

        GambleRig.RigState st = GambleRig.state(p.getUuid());
        for (int i = 0; i < self.size(); i++) {
            ItemStack s = self.getStack(i);
            if (s.isEmpty()) continue;
            if (s.isOf(st.item) == st.win) { cir.setReturnValue(i); return; }
        }
        // no matching slot: vanilla behaviour
    }
}
