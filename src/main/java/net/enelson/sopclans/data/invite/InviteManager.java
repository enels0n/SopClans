package net.enelson.sopclans.data.invite;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;

import net.enelson.sopclans.SopClans;

public class InviteManager {
	List<Invite> invites;
	
	public InviteManager() {
		this.invites = SopClans.sql.getInvites();
	}
	
	public Invite getInvite(int clanId, String invitedUuid) {
		return this.invites.stream().filter(i -> i.getClanId() == clanId && i.getInvitedUuid().equals(invitedUuid)).findFirst().orElse(null);
	}
	
	public Invite createInvite(int clanId, Player inviter, Player invited, Long inviteTime) {
		Invite invite = new Invite(clanId, inviter.getUniqueId().toString(), inviter.getName(),
				invited.getUniqueId().toString(), invited.getName(),
				System.currentTimeMillis()/1000);
		this.invites.add(invite);
		SopClans.sql.addInvite(clanId, inviter.getUniqueId().toString(), inviter.getName(), invited.getUniqueId().toString(), invited.getName());
		return invite;
	}
	
	public int getClanInvitesCount(int clanId) {
		return (int)this.invites.stream().filter(i -> i.getClanId() == clanId).count();
	}
	
	public List<Invite> getInvites(Player player) {
		return this.invites.stream().filter(i -> i.getInvitedUuid().equals(player.getUniqueId().toString())).collect(Collectors.toList());
	}
	
	public void removeInvite(Invite invite) {
		SopClans.sql.removeInvite(invite.getClanId(), invite.getInvitedUuid());
		this.invites.remove(invite);
	}
}
