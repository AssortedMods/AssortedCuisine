package com.grim3212.assorted.kitchen;

import com.grim3212.assorted.kitchen.client.KitchenClient;
import net.fabricmc.api.ClientModInitializer;

public class AssortedKitchenFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        KitchenClient.init();
    }
}
