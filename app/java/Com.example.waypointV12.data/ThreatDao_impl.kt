package com.example.waypointv12.`data`

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class ThreatDao_Impl(
  __db: RoomDatabase,
) : ThreatDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfThreat: EntityInsertAdapter<Threat>
  init {
    this.__db = __db
    this.__insertAdapterOfThreat = object : EntityInsertAdapter<Threat>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `threats` (`id`,`timestamp`,`ipAddress`,`port`,`threatType`,`networkName`) VALUES (nullif(?, 0),?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: Threat) {
        statement.bindLong(1, entity.id)
        statement.bindLong(2, entity.timestamp)
        statement.bindText(3, entity.ipAddress)
        statement.bindLong(4, entity.port.toLong())
        statement.bindText(5, entity.threatType)
        statement.bindText(6, entity.networkName)
      }
    }
  }

  public override suspend fun insertThreat(threat: Threat): Unit = performSuspending(__db, false,
      true) { _connection ->
    __insertAdapterOfThreat.insert(_connection, threat)
  }

  public override fun getAllThreats(): Flow<List<Threat>> {
    val _sql: String = "SELECT * FROM threats ORDER BY timestamp DESC"
    return createFlow(__db, false, arrayOf("threats")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTimestamp: Int = getColumnIndexOrThrow(_stmt, "timestamp")
        val _columnIndexOfIpAddress: Int = getColumnIndexOrThrow(_stmt, "ipAddress")
        val _columnIndexOfPort: Int = getColumnIndexOrThrow(_stmt, "port")
        val _columnIndexOfThreatType: Int = getColumnIndexOrThrow(_stmt, "threatType")
        val _columnIndexOfNetworkName: Int = getColumnIndexOrThrow(_stmt, "networkName")
        val _result: MutableList<Threat> = mutableListOf()
        while (_stmt.step()) {
          val _item: Threat
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpTimestamp: Long
          _tmpTimestamp = _stmt.getLong(_columnIndexOfTimestamp)
          val _tmpIpAddress: String
          _tmpIpAddress = _stmt.getText(_columnIndexOfIpAddress)
          val _tmpPort: Int
          _tmpPort = _stmt.getLong(_columnIndexOfPort).toInt()
          val _tmpThreatType: String
          _tmpThreatType = _stmt.getText(_columnIndexOfThreatType)
          val _tmpNetworkName: String
          _tmpNetworkName = _stmt.getText(_columnIndexOfNetworkName)
          _item = Threat(_tmpId,_tmpTimestamp,_tmpIpAddress,_tmpPort,_tmpThreatType,_tmpNetworkName)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteAll() {
    val _sql: String = "DELETE FROM threats"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
