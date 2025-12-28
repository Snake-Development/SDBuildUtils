package org.killersnake.sdbuildutils.tasks;

import com.destroystokyo.paper.ParticleBuilder;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.killersnake.sdbuildutils.SDBuildUtils;

public class PersistentParticleTask implements Runnable {

	private final SDBuildUtils plugin;
	private final Location location;
	private final Player player;

	public PersistentParticleTask(SDBuildUtils plugin, Location location, Player player) {
		this.plugin = plugin;
		this.location = location;
		this.player = player;
	}

	@Override
	public void run() {
		Location loc = location.clone();
		new ParticleBuilder(Particle.FLAME)
				.location(loc.add(0, 0.5, 0))
				.count(16)
				.extra(0)
				.offset(0.1, 0.1, 0.1)
				.receivers(player)
				.spawn();
	}
//		new ParticleBuilder(Particle.FLAME)
//				.location(location)
//				.count(5)
//				.offset(0, 0, 0)
//				.receivers(player)
//				.spawn();
}
