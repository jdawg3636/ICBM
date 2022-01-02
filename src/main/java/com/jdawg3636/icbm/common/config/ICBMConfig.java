package com.jdawg3636.icbm.common.config;

import net.minecraftforge.common.ForgeConfigSpec;

import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;

public class ICBMConfig {

    /**
     * Client specific configuration - only loaded clientside
     */
    public static class Client {

        public ForgeConfigSpec spec;

        private final BooleanValue testValue;

        public Client() {
            ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
            this.testValue = builder
                    .comment("This is a test of the Forge config system. This setting does nothing.")
                    .define("testValue", false);
            this.spec = builder.build();
        }

        public boolean getTestValue() {
            return testValue.get();
        }

    }

    /**
     * General configuration that doesn't need to be synchronized but needs to be available before server startup
     */
    public static class Common {

        public ForgeConfigSpec spec;

        private final BooleanValue testValue;

        public Common() {
            ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
            this.testValue = builder
                    .comment("This is a test of the Forge config system. This setting does nothing.")
                    .define("testValue", false);
            this.spec = builder.build();
        }

        public boolean getTestValue() {
            return testValue.get();
        }

    }

    /**
     * Synchronized, stored in world file. Ewww.
     */
    public static class Server {

        public ForgeConfigSpec spec;

        private final BooleanValue testValue;

        public Server() {
            ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
            this.testValue = builder
                    .comment("This is a test of the Forge config system. This setting does nothing.")
                    .define("testValue", false);
            this.spec = builder.build();
        }

        public boolean getTestValue() {
            return testValue.get();
        }

    }

}
