package net.enelson.sopclans.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import net.enelson.sopclans.SopClans;
import net.enelson.sopclans.data.clan.Clan;
import net.enelson.sopclans.data.clanwars.ClanWar;
import net.enelson.sopclans.data.invite.Invite;
import net.enelson.sopclans.data.member.Member;
import net.enelson.sopclans.data.rank.Rank;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLManager {

	private Connection conn;
	private Plugin plugin;

	public SQLManager(Plugin plugin) {
		this.plugin = plugin;
		this.conn = getConn();
		this.initDatabase();
	}

	private Connection getConn() {
        try {
            String url = "jdbc:sqlite:" + plugin.getDataFolder().getAbsolutePath() + "/database.db"; // Р Р€Р С”Р В°Р В·РЎвЂ№Р Р†Р В°Р ВµР С Р С—РЎС“РЎвЂљРЎРЉ Р С” Р В±Р В°Р В·Р Вµ Р Т‘Р В°Р Р…Р Р…РЎвЂ№РЎвЂ¦ SQLite
            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
            Bukkit.getServer().getPluginManager().disablePlugin(this.plugin);
            return null;
        }
	}
	
    public int addClan(String clanName, int slots) {
        try {
            if (conn == null || conn.isClosed()) {
                conn = getConn();
            }

            String query = "INSERT INTO clans (name, slots) VALUES (?, ?)";

            PreparedStatement e = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            e.setString(1, clanName);
            e.setInt(2, slots);

            e.executeUpdate();
            this.conn.commit();
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

			String query = "UPDATE `clans` SET `"+par.getTextParameter()+"`=? WHERE `id` = ?;";

			PreparedStatement e = this.conn.prepareStatement(query);
			e.setObject(1, value);
			e.setInt(2, clanId);
			e.executeUpdate();
			this.conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    public List<Clan> getClans() {
        try {
            if (conn == null || conn.isClosed()) {
                conn = getConn();
            }
            List<Clan> clans = new ArrayList<>();
            String query = "SELECT * FROM clans WHERE isActive = 1";

            PreparedStatement e = conn.prepareStatement(query);
            ResultSet rs = e.executeQuery();

            while (rs.next()) {
                clans.add(new Clan(rs.getInt("id"), rs.getString("name"), rs.getString("tag"), 
                                   rs.getInt("level"), rs.getDouble("exp"), rs.getInt("slots"), rs.getBoolean("isPvpEnabled"), rs.getString("clanhome")));
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

			String query = "INSERT INTO clanmembers (clanId, uuid, playerName, rank, exp) VALUES (?, ?, ?, ?, ?);";

			PreparedStatement e = this.conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			e.setInt(1, clanId);
			e.setString(2, member.getUniqueId());
			e.setString(3, member.getPlayerName());
			e.setString(4, member.getRank().getName());
			e.setDouble(5, member.getExp());

			e.executeUpdate();
			ResultSet result = e.getGeneratedKeys();
			this.conn.commit();
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

			String query = "UPDATE `clanmembers` SET `isActive`=0 WHERE `clanId` = ? AND `uuid` = ?;";

			PreparedStatement e = this.conn.prepareStatement(query);
			e.setInt(1, member.getClanId());
			e.setString(2, member.getUniqueId());
			e.executeUpdate();
			this.conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void removeMembers(int clanId) {
		try {
			if (!(this.conn != null && !this.conn.isClosed())) {
				this.conn = this.getConn();
			}

			String query = "UPDATE `clanmembers` SET `isActive`=0 WHERE `clanId` = ?;";

			PreparedStatement e = this.conn.prepareStatement(query);
			e.setInt(1, clanId);
			e.executeUpdate();
			this.conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateMemberParameter(int clanId, String uuid, SQLMemberParameter par, Object value) {
		try {
			if (!(this.conn != null && !this.conn.isClosed())) {
				this.conn = this.getConn();
			}

			String query = "UPDATE `clanmembers` SET `"+par.getTextParameter()+"`=? WHERE `clanId` = ? AND `uuid`=? AND `isActive`=1;";

			PreparedStatement e = this.conn.prepareStatement(query);
			e.setObject(1, value);
			e.setInt(2, clanId);
			e.setString(3, uuid);
			e.executeUpdate();
			this.conn.commit();
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
				query = "INSERT INTO `clanwars` (attackerClanId, defenderClanId, startTime, attackerMultiplier, defenderMultiplier, isActive) VALUES (?, ?, ?, ?, ?, ?)";
			else
				query = "UPDATE `clanwars` SET `isActive` = 0 WHERE `attackerClanId` = ? AND `defenderClanId` = ? AND `isActive` = 1";
			
			PreparedStatement e = this.conn.prepareStatement(query);
			e.setInt(1, attackerClanId);
			e.setInt(2, defenderClanId);
			if(start) {
				e.setLong(3, startTime);
				e.setDouble(4, attackerMultiplier);
				e.setDouble(5, defenderMultiplier);
				e.setBoolean(6, true);
			}

			e.executeUpdate();
			this.conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
    public List<ClanWar> getClanWars() {
        List<ClanWar> wars = new ArrayList<>();
        try {
            if (!(this.conn != null && !this.conn.isClosed())) {
                this.conn = this.getConn();
            }

            String query = "SELECT attackerClanId, defenderClanId, startTime, attackerMultiplier, defenderMultiplier FROM `clanwars` WHERE `isActive` = 1";
            PreparedStatement statement = this.conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int attackerClanId = resultSet.getInt("attackerClanId");
                int defenderClanId = resultSet.getInt("defenderClanId");
                Long startTime = resultSet.getLong("startTime");
                double attackerMultiplier = resultSet.getDouble("attackerMultiplier");
                double defenderMultiplier = resultSet.getDouble("defenderMultiplier");

                ClanWar war = new ClanWar(attackerClanId, defenderClanId, startTime, attackerMultiplier, defenderMultiplier);
                wars.add(war);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

        return wars;
    }

	public List<Member> getMembers(int clanId) {
		try {
			if (!(this.conn != null && !this.conn.isClosed())) {
				this.conn = this.getConn();
			}
			List<Member> members = new ArrayList<Member>();
			String query = "SELECT * FROM `clanmembers` WHERE `clanId`=? AND `isActive`=1";

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

            String query = "INSERT INTO `invites` (clanId, inviterUuid, inviterName, invitedUuid, invitedName) VALUES (?, ?, ?, ?, ?)";

			PreparedStatement e = this.conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			e.setInt(1, clanId);
			e.setString(2, inviterUuid);
			e.setString(3, inviterName);
			e.setString(4, invitedUuid);
			e.setString(5, invitedName);

			e.executeUpdate();
			ResultSet result = e.getGeneratedKeys();
			this.conn.commit();
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

			String query = "UPDATE `invites` SET `isActive`=0 WHERE `clanId` = ? AND `invitedUuid` = ?;";

			PreparedStatement e = this.conn.prepareStatement(query);
			e.setInt(1, clanId);
			e.setString(2, invitedUuid);
			e.executeUpdate();
			this.conn.commit();
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
			String query = "SELECT * FROM `invites` WHERE `isActive`=1";

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
	
	private void initDatabase() {
	    try {
	        if (conn == null || conn.isClosed()) {
	            conn = getConn();
	        }
	        
	        // Р РЋР С•Р В·Р Т‘Р В°Р Р…Р С‘Р Вµ РЎвЂљР В°Р В±Р В»Р С‘РЎвЂ РЎвЂ№ Р Т‘Р В»РЎРЏ Р С”Р В»Р В°Р Р…Р С•Р Р†
	        String createClansTable = "CREATE TABLE IF NOT EXISTS clans (" +
	                                   "id INTEGER PRIMARY KEY AUTOINCREMENT," +
	                                   "name TEXT NOT NULL," +
	                                   "tag TEXT," +
	                                   "level INTEGER DEFAULT 1," +
	                                   "exp REAL DEFAULT 0," +
	                                   "slots INTEGER DEFAULT 5," +
	                                   "isActive INTEGER DEFAULT 1," +
	                                   "isPvpEnabled INTEGER DEFAULT 0," +
	                                   "clanhome TEXT" +
	                                   ");";
	        
	        // Р РЋР С•Р В·Р Т‘Р В°Р Р…Р С‘Р Вµ РЎвЂљР В°Р В±Р В»Р С‘РЎвЂ РЎвЂ№ Р Т‘Р В»РЎРЏ РЎвЂЎР В»Р ВµР Р…Р С•Р Р† Р С”Р В»Р В°Р Р…Р В°
	        String createMembersTable = "CREATE TABLE IF NOT EXISTS clanmembers (" +
	                                     "id INTEGER PRIMARY KEY AUTOINCREMENT," +
	                                     "clanId INTEGER," +
	                                     "uuid TEXT NOT NULL," +
	                                     "playerName TEXT NOT NULL," +
	                                     "rank TEXT," +
	                                     "exp REAL DEFAULT 0," +
	                                     "isActive INTEGER DEFAULT 1," +
	                                     "FOREIGN KEY (clanId) REFERENCES clans(id) ON DELETE CASCADE" +
	                                     ");";

	        // Р РЋР С•Р В·Р Т‘Р В°Р Р…Р С‘Р Вµ РЎвЂљР В°Р В±Р В»Р С‘РЎвЂ РЎвЂ№ Р Т‘Р В»РЎРЏ Р С—РЎР‚Р С‘Р С–Р В»Р В°РЎРѓР С‘РЎвЂљР ВµР В»РЎРЉР Р…РЎвЂ№РЎвЂ¦
	        String createInvitesTable = "CREATE TABLE IF NOT EXISTS invites (" +
	                                     "id INTEGER PRIMARY KEY AUTOINCREMENT," +
	                                     "clanId INTEGER," +
	                                     "inviterUuid TEXT NOT NULL," +
	                                     "inviterName TEXT NOT NULL," +
	                                     "invitedUuid TEXT NOT NULL," +
	                                     "invitedName TEXT NOT NULL," +
	                                     "inviteTime INTEGER DEFAULT CURRENT_TIMESTAMP," +
	                                     "isActive INTEGER DEFAULT 1," +
	                                     "FOREIGN KEY (clanId) REFERENCES clans(id) ON DELETE CASCADE" +
	                                     ");";

	        // Р РЋР С•Р В·Р Т‘Р В°Р Р…Р С‘Р Вµ РЎвЂљР В°Р В±Р В»Р С‘РЎвЂ РЎвЂ№ Р Т‘Р В»РЎРЏ Р Р†Р С•Р в„–Р Р… Р С”Р В»Р В°Р Р…Р С•Р Р†
	        String createClanWarsTable = "CREATE TABLE IF NOT EXISTS clanwars (" +
	                                      "attackerClanId INTEGER NOT NULL," +
	                                      "defenderClanId INTEGER NOT NULL," +
	                                      "startTime INTEGER NOT NULL," +
	                                      "attackerMultiplier REAL NOT NULL," +
	                                      "defenderMultiplier REAL NOT NULL," +
	                                      "isActive INTEGER DEFAULT 1," +
	                                      "PRIMARY KEY (attackerClanId, defenderClanId, startTime)," +  // Р РЋР С•РЎРѓРЎвЂљР В°Р Р†Р Р…Р С•Р в„– Р С—Р ВµРЎР‚Р Р†Р С‘РЎвЂЎР Р…РЎвЂ№Р в„– Р С”Р В»РЎР‹РЎвЂЎ
	                                      "FOREIGN KEY (attackerClanId) REFERENCES clans(id) ON DELETE CASCADE," +
	                                      "FOREIGN KEY (defenderClanId) REFERENCES clans(id) ON DELETE CASCADE" +
	                                      ");";
	        
	        // Р вЂ™РЎвЂ№Р С—Р С•Р В»Р Р…Р ВµР Р…Р С‘Р Вµ Р В·Р В°Р С—РЎР‚Р С•РЎРѓР С•Р Р†
	        Statement stmt = conn.createStatement();
	        stmt.execute(createClansTable);
	        stmt.execute(createMembersTable);
	        stmt.execute(createInvitesTable);
	        stmt.execute(createClanWarsTable);
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	public void disconnect() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
