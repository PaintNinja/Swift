package ga.ozli.minecraftmods.swift.common.sodium.util.matrix;

import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Quaternion;

public interface Matrix3fExtended {
    /**
     * Applies the specified rotation to this matrix in-place.
     *
     * @param quaternion The quaternion to rotate this matrix by
     */
    void rotate(Quaternion quaternion);

    int computeNormal(Direction dir);
}
