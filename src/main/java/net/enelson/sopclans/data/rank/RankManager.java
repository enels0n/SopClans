package net.enelson.sopclans.data.rank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.enelson.sopclans.SopClans;

public class RankManager {
	private List<Rank> ranks;
	
	public RankManager() {
		this.ranks = new ArrayList<Rank>();
		for(String r : SopClans.configMain.getConfigurationSection("clan.ranks").getKeys(false)) {
			SopClans.configMain.getString("ranks");
			HashMap<String,Boolean> permissions = new HashMap<String,Boolean>();
			for(String perm : SopClans.configMain.getConfigurationSection("clan.ranks."+r).getKeys(false)) {
				if(!perm.equals("display_name")) {
					permissions.put(perm, SopClans.configMain.getBoolean("clan.ranks."+r+"."+perm));
				}
			}

			Double promotePrice = (SopClans.economy == null) ? 0 : SopClans.configMain.getDouble("economy.promote_price."+r);
			Double demotePrice = (SopClans.economy == null) ? 0 : SopClans.configMain.getDouble("economy.demote_price."+r);
			Rank rank = new Rank(r, SopClans.configMain.getString("clan.ranks."+r+".display_name"), permissions, promotePrice, demotePrice);
			this.ranks.add(rank);
		}
	}
	
	public Rank getRank(String name) {
		return this.ranks.stream().filter(r -> r.getName().equals(name)).findFirst().orElse(null);
	}
	
	public List<Rank> getRanks() {
		return this.ranks;
	}
	
	public Rank getDefaultRank() {
		for(int i = this.ranks.size()-1; i>0; i--) {
			if(this.ranks.get(i).getPermission("default"))
				return this.ranks.get(i);
		}
		return null;
	}
	
	public Rank getNextRank(Rank currentRank) {
		int index = this.ranks.indexOf(currentRank)-1;
		return (index<=0) ? null : this.ranks.get(index);
	}
	
	public Rank getPreviousRank(Rank currentRank) {
		int index = this.ranks.indexOf(currentRank)+1;
		return (index>=this.ranks.size()) ? null : this.ranks.get(index);
	}
	
	public int getRankPriority(Rank rank) {
		return this.ranks.indexOf(rank);
	}
}
