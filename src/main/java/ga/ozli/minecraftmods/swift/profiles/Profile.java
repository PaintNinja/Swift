package ga.ozli.minecraftmods.swift.profiles;

import ga.ozli.minecraftmods.swift.Swift;
import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.AmbientOcclusionStatus;
import net.minecraft.client.settings.CloudOption;
import net.minecraft.client.settings.GraphicsFanciness;
import net.minecraft.client.settings.ParticleStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Profile {

    private static final Logger LOGGER = LogManager.getLogger(Swift.MODID + " Profiles");

    private int renderDistance;
    private ParticleStatus particles;
    private int maxFps;
    private GraphicsFanciness graphicsFanciness;
    private AmbientOcclusionStatus ambientOcclusion;
    private CloudOption clouds;
    private boolean vsync;
    private boolean pauseOnLostFocus;
    private int mipmapLevels;
    private final Map<String, Object> optifine = new HashMap<>(); // todo: set initial capacity

    private void fromGameSettingsOptifine(GameSettings gameSettings) {
        for(Field field : gameSettings.getClass().getFields()) {
            String name = field.getName();
            if(name.startsWith("of") && !name.equals("ofKeyBindZoom"))
                try {
                    this.optifine.put(name, field.get(gameSettings));
                } catch(Exception e) {
                    LOGGER.error(String.format("Can not get property '%s'!", name), e);
                }
        }
    }

    private void toGameSettingsOptifine(GameSettings gameSettings) {
        for (Field field : gameSettings.getClass().getFields()) {
            String name = field.getName();
            if (name.startsWith("of") && !name.equals("ofKeyBindZoom"))
                try {
                    Object value = this.optifine.get(name);

                    // JSON loads numbers as Double, have to correct that
                    // todo: reevaluate if this code is still needed as some types are now doubles
                    if (value instanceof Number)
                        if (field.getType() == int.class)
                            value = ((Number) value).intValue();
                        else if (field.getType() == float.class)
                            value = ((Number) value).floatValue();

                    if (value != null)
                        field.set(gameSettings, value);
                } catch (Exception e) {
                    LOGGER.error(String.format("Can not set property '%s'!", name), e);
                }
        }
    }

    public static Profile fromGameSettings(GameSettings gameSettings) {
        Profile profile = new Profile();

        profile.renderDistance = gameSettings.renderDistanceChunks;
        profile.particles = gameSettings.particles;
        profile.maxFps = gameSettings.framerateLimit;
        //profile.fbo = gameSettings.fboEnable;
        profile.graphicsFanciness = gameSettings.field_238330_f_;
        profile.ambientOcclusion = gameSettings.ambientOcclusionStatus;
        profile.clouds = gameSettings.getCloudOption();
		//profile.fullscreen = gameSettings.fullscreen;
        profile.vsync = gameSettings.vsync;
        profile.pauseOnLostFocus = gameSettings.pauseOnLostFocus;
        profile.mipmapLevels = gameSettings.mipmapLevels;
        //profile.anisotropicFiltering = gameSettings.anisotropicFiltering;

        // todo: detect Optifine
        /*if (FMLClientHandler.instance().hasOptifine())
            profile.fromGameSettingsOptifine(gameSettings);*/
        return profile;
    }

    public static GameSettings toGameSettings(Profile profile) {
        GameSettings gameSettings = Minecraft.getInstance().gameSettings;

        profile.renderDistance = gameSettings.renderDistanceChunks;
        profile.particles = gameSettings.particles;
        profile.maxFps = gameSettings.framerateLimit;
        //profile.fbo = gameSettings.fboEnable;
        profile.graphicsFanciness = gameSettings.field_238330_f_;
        profile.ambientOcclusion = gameSettings.ambientOcclusionStatus;
        profile.clouds = gameSettings.cloudOption;
        //profile.fullscreen = gameSettings.fullscreen;
        profile.vsync = gameSettings.vsync;
        profile.pauseOnLostFocus = gameSettings.pauseOnLostFocus;
        profile.mipmapLevels = gameSettings.mipmapLevels;
        //profile.anisotropicFiltering = gameSettings.anisotropicFiltering;

        /*if (FMLClientHandler.instance().hasOptifine())
            profile.toGameSettingsOptifine(gameSettings);*/
        return gameSettings;
    }
}
