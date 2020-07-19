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
    - `core.frustum.MixinFrustum`
    - `core.matrix.MixinMatrix3f`
    - `core.matrix.MixinMatrix4f`
    - `features.block.MixinBufferBuilder`
    - `features.buffer_builder.fast_advance.MixinBufferBuilder`
    - `features.buffer_builder.fast_sort.MixinBufferBuilder`
    - `features.buffer_builder.intrinsics.MixinBufferBuilder`
    - `features.buffer_builder.intrinsics.MixinSpriteTexturedVertexConsumer`
    - `features.gui.font.MixinGlyphRenderer`
    - `features.matrix_stack.MixinMatrixStack`
    - `features.matrix_stack.MixinVertexConsumer`
- Lithium
    - `cached_hashcode.BlockNeighborGroupMixin`
    - `chunk.serialization.PackedIntegerArrayMixin`
    - `chunk.serialization.PalettedContainerMixin`
    - `math.fast_util.AxisCycleDirectionMixin$BackwardMixin`
    - `math.fast_util.AxisCycleDirectionMixin$ForwardMixin`
    - `math.fast_util.BoxMixin`
    - `math.fast_util.DirectionMixin`
    - `shapes.blockstate_cache.AbstractBlockStateMixin`
    - `shapes.blockstate_cache.BlockMixin`
    - `shapes.blockstate_cache.BlockShapeCacheMixin`
    - `shapes.precompute_shape_arrays.SimpleVoxelShapeMixin`
    - `shapes.shape_merging.VoxelShapesMixin`
        - Thanks to access transfarmer and others on the Fabric discord for helping me verify that the mixins were actually working by registering breakpoints in the right spots
    - `shapes.specialized_shapes.VoxelShapeMixin`
    - `shapes.specialized_shapes.VoxelShapesMixin`
        - Thanks to gigaherz on the MMD discord for helping me get an access transformer for a constructor for this to work

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
I intentionally avoid changing the names of any functions or variables when not necessary. This in turn can lead to somewhat more confusing code for someone only versed in Forge mappings, but the benefit is that new optimisations and changes on the Fabric version can be more easily ported to Forge in the future.

Why are you unofficially maintaining Forge ports of jellysquid3's mods?
-----------------------------------------------------------------------
There are various big Forge mods that aren't on Fabric yet, making it unrealistic for everyone to move over from Forge just so they can take advantage of some of the great optimisations that have been made elsewhere. I'm just buying those big Forge mod devs some extra time.

As this is an unofficial port, it may be buggier than the official Fabric versions of the mods. **Don't contact jellysquid3 for help with this Forge port, she's not involved with this port in any shape or form.**

Once all the mods you want (or alternatives of those mods) are available on Fabric, I strongly recommend that you move to Fabric instead of relying on this unofficial, unsupported port that may become too hard for me to maintain once jellysquid3's mods become reliant on Fabric-exclusive functionality, such as the FRAPI.