package net.enelson.sopclans.data.invite;

public class Invite {
	
	private int clanId;
	private String invitedUuid;
	private String invitedName;
	private Long inviteTime;
	private String inviterUuid;
	private String inviterName;
	
	public Invite(int clanId, String inviterUuid, String inviterName, String invitedUuid, String invitedName, Long inviteTime) {
		this.clanId = clanId;
		this.invitedUuid = invitedUuid;
		this.invitedName = invitedName;
		this.inviteTime = inviteTime;
		this.inviterUuid = inviterUuid;
		this.inviterName = inviterName;
	}
	
	public int getClanId() {
		return this.clanId;
	}
	
	public String getInvitedUuid() {
		return this.invitedUuid;
	}
	
	public String getInvitedName() {
		return this.invitedName;
	}
	
	public Long getInviteTime() {
		return this.inviteTime;
	}
	
	public String getInviterUuid() {
		return this.inviterUuid;
	}
	
	public String getInviterName() {
		return this.inviterName;
	}
	
}
