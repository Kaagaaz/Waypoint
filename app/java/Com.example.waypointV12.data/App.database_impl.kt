package com.example.waypointv12.`data`

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class AppDatabase_Impl : AppDatabase() {
  private val _threatDao: Lazy<ThreatDao> = lazy {
    ThreatDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(1,
        "4aa94b1c304feb157a85fe7d155362e7", "edb7c8483dc2111ff849e97e127e9f53") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `threats` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `timestamp` INTEGER NOT NULL, `ipAddress` TEXT NOT NULL, `port` INTEGER NOT NULL, `threatType` TEXT NOT NULL, `networkName` TEXT NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '4aa94b1c304feb157a85fe7d155362e7')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `threats`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection):
          RoomOpenDelegate.ValidationResult {
        val _columnsThreats: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsThreats.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsThreats.put("timestamp", TableInfo.Column("timestamp", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsThreats.put("ipAddress", TableInfo.Column("ipAddress", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsThreats.put("port", TableInfo.Column("port", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsThreats.put("threatType", TableInfo.Column("threatType", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsThreats.put("networkName", TableInfo.Column("networkName", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysThreats: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesThreats: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoThreats: TableInfo = TableInfo("threats", _columnsThreats, _foreignKeysThreats,
            _indicesThreats)
        val _existingThreats: TableInfo = read(connection, "threats")
        if (!_infoThreats.equals(_existingThreats)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |threats(com.example.waypointv12.data.Threat).
              | Expected:
              |""".trimMargin() + _infoThreats + """
              |
              | Found:
              |""".trimMargin() + _existingThreats)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "threats")
  }

  public override fun clearAllTables() {
    super.performClear(false, "threats")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(ThreatDao::class, ThreatDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override
      fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>):
      List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun threatDao(): ThreatDao = _threatDao.value
}
