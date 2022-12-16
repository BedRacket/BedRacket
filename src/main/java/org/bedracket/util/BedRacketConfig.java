package org.bedracket.util;

import net.fabricmc.loader.api.FabricLoader;

public class BedRacketConfig {

    @Config(config = "bedracket", category = "misc",
            comment = "The buoyancy of boats when they are under flowing water." +
                    "The vanilla default is -0.0007." +
                    "Setting this to a positive value allows boats to float up when they move into " +
                    "a higher block of water, fixing MC-91206: " +
                    "https://bugs.mojang.com/browse/MC-91206")
    public static double boatBuoyancyUnderFlowingWater =
            FabricLoader.getInstance().isDevelopmentEnvironment() ? 5.0 : 0.023;

    @Config(config = "bedracket", category = "misc",
            comment = "How long it takes in ticks for a boat passenger to be ejected when underwater," +
            "Set this to -1 to disable underwater boat passenger ejection.")
    public static int underwaterBoatPassengerEjectionDelayTicks =
            FabricLoader.getInstance().isDevelopmentEnvironment() ? -1 : 60;

    @Config(config = "bedracket", category = "misc",
            comment = "Fixes boats and players in boats not taking fall damage." +
                    "This bug is reported as both MC-98160: https://bugs.mojang.com/browse/MC-98160" +
                    "and MC-105103: https://bugs.mojang.com/browse/MC-105103")
    public static boolean fixBoatFallDamage = true;

    @Config(config = "bedracket", category = "misc",
            comment = "Fixes animals which can breed only showing hearts once initially instead of " +
                    "continuously." +
                    "This bug is reported as MC-93826: https://bugs.mojang.com/browse/MC-93826")
    public static boolean fixAnimalBreedingHearts = true;

    @Config(config = "bedracket", category = "growth-rates",
            comment = "Decide bamboo growth rate")
    public static int bambooModifier = 100;

    @Config(config = "bedracket", category = "growth-rates",
            comment = "Decide cocoa growth rate")
    public static int cocoaModifier = 100;

    @Config(config = "bedracket", category = "growth-rates",
            comment = "Decide beetroot growth rate")
    public static int beetrootModifier = 100;

    @Config(config = "bedracket", category = "growth-rates",
            comment = "Decide carrot growth rate")
    public static int carrotModifier = 100;

    @Config(config = "bedracket", category = "growth-rates",
            comment = "Decide potato growth rate")
    public static int potatoModifier = 100;

    @Config(config = "bedracket", category = "growth-rates",
            comment = "Decide wheat growth rate")
    public static int wheatModifier = 100;

}
