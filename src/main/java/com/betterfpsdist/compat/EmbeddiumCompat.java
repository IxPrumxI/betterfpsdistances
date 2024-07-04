package com.betterfpsdist.compat;

import org.embeddedt.embeddium.api.OptionPageConstructionEvent;
import org.embeddedt.embeddium.api.render.chunk.RenderSectionDistanceFilterEvent;

public class EmbeddiumCompat
{
    public static void initCompat()
    {
        OptionPageConstructionEvent.BUS.addListener(EmbeddiumEventHandler::on);
        RenderSectionDistanceFilterEvent.BUS.addListener(EmbeddiumEventHandler::distanceFilterEvent);
    }
}
