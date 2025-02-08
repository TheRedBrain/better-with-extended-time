package com.github.theredbrain.betterwithextendedtime.config;

import com.github.theredbrain.betterwithextendedtime.BetterWithExtendedTime;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;

public class ServerConfig extends Config {
	public ServerConfig() {
		super(BetterWithExtendedTime.identifier("server"));
	}

	public ValidatedBoolean hand_cranking_requires_stamina = new ValidatedBoolean(true);
	public ValidatedBoolean hand_cranking_requires_stamina_cost = new ValidatedBoolean(true);
	public ValidatedFloat hand_cranking_stamina_cost = new ValidatedFloat(10.0F);
}
