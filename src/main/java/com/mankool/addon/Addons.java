package com.mankool.addon;

import com.mankool.addon.modules.CoordDC;
import com.mojang.logging.LogUtils;
import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.hud.HudGroup;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import org.slf4j.Logger;

public class Addons extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();
    public static final Category CATEGORY = new Category("mankool");
    public static final HudGroup HUD_GROUP = new HudGroup("mankool");

    @Override
    public void onInitialize() {
        LOG.info("Initializing mankool's Meteor Addons");

        // Modules
        Modules.get().add(new CoordDC());

        // Commands
        //Commands.add(new CommandExample());

        // HUD
        //Hud.get().register(HudExample.INFO);
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(CATEGORY);
    }

    @Override
    public String getPackage() {
        return "com.mankool.addon";
    }

    @Override
    public GithubRepo getRepo() {
        return new GithubRepo("mankool0", "metor-addons");
    }
}
