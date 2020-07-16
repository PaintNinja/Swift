Swift
=====
Dynamic optimisation mod for Minecraft 1.16.1 (Forge)

What does it do?
----------------
Swift is an attempt at combining some of the best existing community optimisation techniques - whether it's from an old abandoned mod or from a different modding platform entirely (e.g. Fabric) - while also making sure that compatibility is maintained with other mods such as Optifine's shaderpack feature.

In addition to this, I've provided some of my own optimisations and features to the table for Swift, such as seeing the average and 1% lows in the F3 debug menu and dynamically switching graphics settings on the fly to maintain more consistent performance in a heavily computationally-variable game.

What's included?
----------------
(temp text here until I write a proper desc. for this section)

Done:
- Sodium
    - `fast_mojmath.MixinFrustum`
    - `fast_mojmath.MixinMatrix3f`
    - `fast_mojmath.MixinMatrix4f`
    - `fast_mojmath.MixinMatrixStack`
- Lithium
    - `cached_hashcode.BlockNeighborGroupMixin`
    - `math.fast_util.AxisCycleDirectionMixin$BackwardMixin`
    - `math.fast_util.AxisCycleDirectionMixin$ForwardMixin`
    - `math.fast_util.BoxMixin`
    - `math.fast_util.DirectionMixin`
    - `shapes.blockstate_cache.BlockMixin`
    - `shapes.precompute_shape_arrays.SimpleVoxelShapeMixin`
    - `shapes.shape_merging.VoxelShapesMixin`
    - `shapes.specialized_shapes.VoxelShapeMixin`
    - `shapes.specialized_shapes.VoxelShapesMixin`
        - Thanks to gigaherz on the MMD discord for helping me get an access transformer for a constructor for this to work

On the backburner:
(these rely on things that seem to be *very* different between Forge and Fabric)
- Sodium
    - `buffers.MixinBufferBuilder`
    - `buffers.MixinSpriteTexturedVertexConsumer`

Todo:
- A complete port other jellysquid3's excellent Lithium and Phosphor 1.16 mods for Fabric to Forge
    - While jellysquid3 does offer Lithium and Phosphor for 1.15 Forge, they are very stripped down and outdated compared to their Fabric counterparts and there is no sign of them being updated for 1.16 Forge.
- Portions of jellysquid3's Sodium 1.16 Fabric mod are being ported and then tested in isolation for compatibility and those that play nice with other mods are kept.

How do you do it?
-----------------
Porting across different Minecraft versions as well as different mappings of those versions and different modding platforms is no small feat.

It's not a simple copy and paste and renaming a few imports to point to the right place - some parts of the Minecraft code behind-the-scenes are fundamentally different and need to be accounted for when the versions of MC the optimisations I'm porting from are different to what I'm porting to.

In cases where I'm porting from the same Minecraft version but across different mappings and modding platforms, the process is simpler but still very tedious at best:

I have to guess what the mapping is in Forge and then do a comparison between the decompiled Forge and Fabric mappings to make sure that I actually have the right files as sometimes the names may match but be in different places but actually be for completely separate purposes. For example, Fabric 1.16 has `net.minecraft.client.render.Frustum` and Forge 1.16 has `net.minecraft.client.renderer.ViewFrustum` - but the correct Forge mapping of Fabric's `render.Frustum` is actually `renderer.culling.ClippingHelper` - *NOT* `renderer.ViewFrustum`!

Once I've got that, I can then go about converting the different function names and variables and prey that it works when running the game in the dev instance. Once I've done all that, I have to compile the jar, load up an instance of Forge with Optifine installed (as Optifine doesn't work in a dev environment at all) and test compatibility. If it doesn't work, I strip back a feature, see if it's still broken, if it is I put that feature back and strip a different feature until I identify the individual or combination of features that break compatibility and I remove them from Swift.

What's different about your approach?
-------------------------------------
I intentionally avoid changing the names of any functions or variables when not necessary. This in turn can lead to somewhat more confusing code for someone only versed in Forge mappings, but the benefit is that new optimisations and changes on the Fabric version can be more easily ported to Forge in future.

What does Swift offer that other Forge optimisation mods don't?
---------------------------------------------------------------
(temp text here until I write a proper desc. for this section)
I don't copy over the code from jellysquid3's Forge ports unless (I'm really stuck!) so that I can take advantage of newer optimisations added to the Fabric versions since then.

I also keep comments of the old Fabric imports so that it' easier to map the Fabric changes over to Forge without having to re-lookup the equivalent Forge mappings.

### Here's a list of things that can only be found in Swift if you need to use Forge:
- Ported things from Sodium for Fabric:
    - `fast_mojmath.MixinFrustum`
    - `fast_mojmath.MixinMatrix3f`
    - `fast_mojmath.MixinMatrix4f`
    - `fast_mojmath.MixinMatrixStack`
- Ported things from Lithium for Fabric:
    - Optimized backing cache for Block#isOpaque (https://github.com/jellysquid3/lithium-fabric/commit/c3a449c8ae5b24d686c6525c1d258e92f6efb9c1#diff-c058ef87fb2d3e963703ad3cdb4e909c)
    - TODO: Patches to reduce overhead of entity fluid checks (https://github.com/jellysquid3/lithium-fabric/commit/656e44957d750d71ffe880f4c1bef6b442fb0a88#diff-c058ef87fb2d3e963703ad3cdb4e909c)
- Pretty much all changes and additions to Lithium and Phosphor for Fabric since the official Forge port of them.