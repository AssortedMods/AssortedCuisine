package com.grim3212.assorted.cuisine;

import com.grim3212.assorted.cuisine.client.CuisineClient;
import net.fabricmc.api.ClientModInitializer;

public class AssortedCuisineFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        CuisineClient.init();
    }
}
