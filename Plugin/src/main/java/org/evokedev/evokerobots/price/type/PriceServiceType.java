package org.evokedev.evokerobots.price.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.evokedev.api.PriceService;
import org.evokedev.evokerobots.price.impl.InternalPriceService;
import org.evokedev.evokerobots.price.impl.ShopGuiPlusPriceService;

@AllArgsConstructor @Getter
public enum PriceServiceType {

    SHOPGUIPLUS(new ShopGuiPlusPriceService()),
    INTERNAL(new InternalPriceService());

    private final PriceService service;
}

