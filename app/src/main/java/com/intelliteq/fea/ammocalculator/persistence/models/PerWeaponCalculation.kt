package com.intelliteq.fea.ammocalculator.persistence.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "single_calculation_table")
data class PerWeaponCalculation (

    @PrimaryKey(autoGenerate = true)
    var single_calculationId: Long =0L,

    @ColumnInfo(name = "id_group_calculation")
    var group_calculationID: Long = 0L,

    @ColumnInfo(name = "num_weapons")
    var numberOfWeapons: Int = 0,

    @ColumnInfo(name = "weapon_id_for_calculation")
    var weaponIDCalculation: Long = 0L,

    @ColumnInfo(name = "ammo_id_for_calculation")
    var weaponAmmoIdCalculation: Long = 0L,

    @ColumnInfo(name = "component_ammo_id_for_calculation")
    var componentAmmoIdCalculation: Long = 0L


)