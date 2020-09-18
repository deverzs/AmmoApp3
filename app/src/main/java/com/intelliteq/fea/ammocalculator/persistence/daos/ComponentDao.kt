package com.intelliteq.fea.ammocalculator.persistence.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.intelliteq.fea.ammocalculator.persistence.models.Component


@Dao
interface ComponentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(component: Component)

    @Update
    fun update(component: Component)

    @Delete
    fun delete(component: Component)

    @Query("SELECT * FROM component_table WHERE componentId = :key" )
    fun get(key: Long?): Component

    @Query("SELECT * FROM component_table WHERE component_type_id = :key")
    fun getWithType(key: String) : Component

    @Query("SELECT * FROM component_table")
    fun getAll(): LiveData<Array<Component>>

    @Query("SELECT count(*) FROM component_table")
    fun countAll() : Int

    @Query("SELECT * FROM component_table ORDER BY componentId DESC LIMIT 1" )
    fun getNewComponent() : Component?

    @Query("SELECT count(*) FROM component_table WHERE weapon_for_component = :key")
    fun countAllComponents(key: Long?) : Int

    @Query("SELECT * FROM component_table WHERE weapon_for_component = :key")
    fun getAllComponentsForThisWeapon(key: Long?) : List<Component>
}