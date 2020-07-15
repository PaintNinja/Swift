package ga.ozli.minecraftmods.swift.common.sodium.util.matrix;

import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.Direction;

public class MatrixUtil {
    public static int computeNormal(Matrix3f normalMatrix, Direction facing) {
        return ((ga.ozli.minecraftmods.swift.common.sodium.util.matrix.Matrix3fExtended) (Object) normalMatrix).computeNormal(facing);
    }
}
