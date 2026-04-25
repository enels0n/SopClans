package net.enelson.sopclans.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import net.enelson.sopclans.SopClans;
import net.enelson.sopclans.data.clan.Clan;
import net.enelson.sopclans.data.invite.Invite;
import net.enelson.sopclans.data.member.Member;
import net.enelson.sopclans.data.rank.Rank;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLManager2 {

	private Connection conn;
	private Plugin plugin;
	private int maxLifeTime;
	private String prefix;
	private BukkitTask runTaskSql;

	public SQLManager2(Plugin plugin) {
		this.plugin = plugin;
		this.conn = getConn();
		this.prefix = SopClans.configMain.getString("mysql.prefix");
		this.maxLifeTime = SopClans.configMain.getInt("mysql.maxLifeTime");

		this.runTaskSql = plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new Runnable() {
			@Override
			public void run() {
				try {
					if (conn != null && !conn.isClosed()) {
						conn.createStatement().execute("SELECT 1");
					}
				} catch (SQLException e) {
					conn = getConn();
				}
			}
		}, this.maxLifeTime * 20, this.maxLifeTime * 20);
	}

	private Connection getConn() {
		try {
			Class.forName("com.mysql.jdbc.Driver");

			String url = "jdbc:mysql://" + SopClans.configMain.getString("mysql.host") + ":"
					+ SopClans.configMain.getInt("mysql.port") + "/" + SopClans.configMain.getString("mysql.db")
					+ "?useUnicode=true&characterEncoding=utf8";
			Connection connection = DriverManager.getConnection(url, SopClans.configMain.getString("mysql.user"),
					SopClans.configMain.getString("mysql.pass"));
			return connection;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			Bukkit.getServer().getPluginManager().disablePlugin(this.plugin);
			return null;
		}
	}

	public int addClan(String clanName, int slots) {
		try {
			if (!(this.conn != null && !this.conn.isClosed())) {
				this.conn = this.getConn();
			}

			String query = "INSERT INTO `" + this.prefix + "clans` SET `name` = ?, `slots` = ?";

			PreparedStatement e = this.conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			e.setString(1, clanName);
			e.setInt(2, slots);

			e.executeUpdate();
			ResultSet result = e.getGeneratedKeys();
			
			if (result.next()) {
				return result.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public void updateClanParameter(int clanId, SQLClanParameter par, Object value) {
		try {
			if (!(this.conn != null && !this.conn.isClosed())) {
				this.conn = this.getConn();
			}

			String query = "UPDATE `" + this.prefix + "clans` SET `"+par.getTextParameter()+"`=? WHERE `id` = ?;";

			PreparedStatement e = this.conn.prepareStatement(query);
			e.setObject(1, value);
			e.setInt(2, clanId);
			e.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Clan> getClans() {
		try {
			if (!(this.conn != null && !this.conn.isClosed())) {
				this.conn = this.getConn();
			}
			List<Clan> clans = new ArrayList<Clan>();
			String query = "SELECT * FROM `"+this.prefix+"clans` WHERE `isActive`=1";

			PreparedStatement e = this.conn.prepareStatement(query);
			
			this.conn.setAutoCommit(false);
			ResultSet rs = e.executeQuery();
			int i = 0;
			while (rs.next() && i<100) {
				clans.add(new Clan(rs.getInt("id"), rs.getString("name"), rs.getString("tag"), rs.getInt("level"), rs.getDouble("exp"), rs.getInt("slots"), rs.getBoolean("isPvpEnabled"), rs.getString("clanhome")));
				i++;
			}
			
			return clans;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public int addMember(int clanId, Member member) {
		try {
			if (!(this.conn != null && !this.conn.isClosed())) {
				this.conn = this.getConn();
			}

			String query = "INSERT INTO `" + this.prefix + "clanmembers` SET `clanId` = ?, `uuid` = ?, `playerName` = ?, `rank` = ?, `exp` = ?";

			PreparedStatement e = this.conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			e.setInt(1, clanId);
			e.setString(2, member.getUniqueId());
			e.setString(3, member.getPlayerName());
			e.setString(4, member.getRank().getName());
			e.setDouble(5, member.getExp());

			e.executeUpdate();
			ResultSet result = e.getGeneratedKeys();
			
			if (result.next()) {
				return result.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public void removeMember(Member member) {
		try {
			if (!(this.conn != null && !this.conn.isClosed())) {
				this.conn = this.getConn();
			}

			String query = "UPDATE `" + this.prefix + "clanmembers` SET `isActive`=0 WHERE `clanId` = ? AND `uuid` = ?;";

			PreparedStatement e = this.conn.prepareStatement(query);
			e.setInt(1, member.getClanId());
			e.setString(2, member.getUniqueId());
			e.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void removeMembers(int clanId) {
		try {
			if (!(this.conn != null && !this.conn.isClosed())) {
				this.conn = this.getConn();
			}

			String query = "UPDATE `" + this.prefix + "clanmembers` SET `isActive`=0 WHERE `clanId` = ?;";

			PreparedStatement e = this.conn.prepareStatement(query);
			e.setInt(1, clanId);
			e.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateMemberParameter(int clanId, String uuid, SQLMemberParameter par, Object value) {
		try {
			if (!(this.conn != null && !this.conn.isClosed())) {
				this.conn = this.getConn();
			}

			String query = "UPDATE `" + this.prefix + "clanmembers` SET `"+par.getTextParameter()+"`=? WHERE `clanId` = ? AND `uuid`=? AND `isActive`=1;";

			PreparedStatement e = this.conn.prepareStatement(query);
			e.setObject(1, value);
			e.setInt(2, clanId);
			e.setString(3, uuid);
			e.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setClanWar(int attackerClanId, int defenderClanId, Long startTime, double attackerMultiplier, double defenderMultiplier, boolean start) {
		try {
			if (!(this.conn != null && !this.conn.isClosed())) {
				this.conn = this.getConn();
			}
			
			String query = "";
			if(start)
				query = "INSERT INTO `" + this.prefix + "clanwars` SET `attackerClanId` = ?, `defenderClanId` = ?, `attackerMultiplier` = ?, `defenderMultiplier` = ?";
			else
				query = "UPDATE `" + this.prefix + "clanwars` SET `isActive` = 0 WHERE `attackerClanId` = ? AND `defenderClanId` = ? AND `isActive` = 1";
			
			PreparedStatement e = this.conn.prepareStatement(query);
			e.setInt(1, attackerClanId);
			e.setInt(2, defenderClanId);
			if(start) {
				e.setDouble(3, attackerMultiplier);
				e.setDouble(4, defenderMultiplier);
			}

			e.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Member> getMembers(int clanId) {
		try {
			if (!(this.conn != null && !this.conn.isClosed())) {
				this.conn = this.getConn();
			}
			List<Member> members = new ArrayList<Member>();
			String query = "SELECT * FROM `"+this.prefix+"clanmembers` WHERE `clanId`=? AND `isActive`=1";

			PreparedStatement e = this.conn.prepareStatement(query);
			e.setInt(1, clanId);
			
			this.conn.setAutoCommit(false);
			ResultSet rs = e.executeQuery();
			int i = 0;
			while (rs.next() && i<100) {
				Rank rank = SopClans.rm.getRank(rs.getString("rank"));
				members.add(new Member(rs.getString("uuid"), rs.getString("playerName"),
						rs.getInt("clanId"), rank,
						rs.getDouble("exp")));
				i++;
			}
			
			return members;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public int addInvite(int clanId, String inviterUuid, String inviterName,  String invitedUuid, String invitedName) {
		try {
			if (!(this.conn != null && !this.conn.isClosed())) {
				this.conn = this.getConn();
			}

			String query = "INSERT INTO `" + this.prefix + "invites` SET `clanId` = ?, `inviterUuid` = ?, `inviterName` = ?, `invitedUuid` = ?, `invitedName` = ?;";

			PreparedStatement e = this.conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			e.setInt(1, clanId);
			e.setString(2, inviterUuid);
			e.setString(3, inviterName);
			e.setString(4, invitedUuid);
			e.setString(5, invitedName);

			e.executeUpdate();
			ResultSet result = e.getGeneratedKeys();
			
			if (result.next()) {
				return result.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public void removeInvite(int clanId, String invitedUuid) {
		try {
			if (!(this.conn != null && !this.conn.isClosed())) {
				this.conn = this.getConn();
			}

			String query = "UPDATE `" + this.prefix + "invites` SET `isActive`=0 WHERE `clanId` = ? AND `invitedUuid` = ?;";

			PreparedStatement e = this.conn.prepareStatement(query);
			e.setInt(1, clanId);
			e.setString(2, invitedUuid);
			e.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Invite> getInvites() {
		try {
			if (!(this.conn != null && !this.conn.isClosed())) {
				this.conn = this.getConn();
			}
			List<Invite> invites = new ArrayList<Invite>();
			String query = "SELECT * FROM `"+this.prefix+"invites` WHERE `isActive`=1";

			PreparedStatement e = this.conn.prepareStatement(query);
			
			this.conn.setAutoCommit(false);
			ResultSet rs = e.executeQuery();
			int i = 0;
			while (rs.next() && i<100) {
				invites.add(new Invite(rs.getInt("clanId"),
						rs.getString("inviterUuid"), rs.getString("inviterName"),
						rs.getString("invitedUuid"), rs.getString("invitedName"),
						rs.getLong("inviteTime")));
				i++;
			}
			
			return invites;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void disconnect() {
		try {
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.runTaskSql.cancel();
	}
}
