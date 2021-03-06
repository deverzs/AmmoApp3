package com.intelliteq.fea.ammocalculator.editAmmoInput

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.intelliteq.fea.ammocalculator.persistence.daos.AmmoDao
import com.intelliteq.fea.ammocalculator.persistence.models.Ammo
import kotlinx.coroutines.*

class EditAmmoViewModel(
    private val ammoKey: Long,
    private val ammoDao: AmmoDao
) : ViewModel() {

    //Job and CoroutineScope
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    //reading database
    private var ammoOld = MutableLiveData<Ammo?>()
    var ammoDescriptionHint = MutableLiveData<String>()
    var ammoTypeHint = MutableLiveData<String>()
    var ammoTrainingHint = MutableLiveData<String>()
    var ammoSustainHint = MutableLiveData<String>()
    var ammoSecurityHint = MutableLiveData<String>()
    var ammoLightHint = MutableLiveData<String>()
    var ammoMediumHint = MutableLiveData<String>()
    var ammoHeavyHint = MutableLiveData<String>()

    private val ammoDefault = MutableLiveData<Boolean>()

    private var ammosList = listOf<Ammo>()

    init {
        populateHints()
    }

    private fun initializeAmmos(){
        uiScope.launch {
            ammosList = getListOfAmmosFromDatabase()
        }
    }

    private suspend fun getListOfAmmosFromDatabase() : List<Ammo> {
        return withContext(Dispatchers.IO) {
            val ammo: List<Ammo> = if(ammoOld.value!!.primaryAmmo) {
                ammoDao.getAllWeaponAmmoList(ammoOld.value!!.weaponId)
            } else {
                ammoDao.getComponentAmmosForThisComponent(ammoOld.value!!.componentId)
            }
            ammo
        }
    }

    //default ammo
    fun setDefault(default: Boolean) {
        uiScope.launch {
            if(default) {
                for (ammo in ammosList) {
                    if(ammo.defaultAmmo){
                        ammo.defaultAmmo = false
                        update(ammo)
                    }
                    if(ammo.ammoAutoId == ammoKey) {
                        ammo.defaultAmmo = true
                        update(ammo)
                    }
                }
            }
        }
    }

    private fun populateHints() {
        uiScope.launch {
            ammoDescriptionHint.value = getDescriptionFromDatabase()
            ammoTypeHint.value = getTypeFromDatabase()
            ammoOld.value = getAmmoFromDatabase()
            ammoHeavyHint.value = getHeavyFromDatabase()
            ammoLightHint.value = getLightFromDatabase()
            ammoMediumHint.value = getMediumFromDatabse()
            ammoSecurityHint.value = getSecurityFromDatabase()
            ammoSustainHint.value = getSustainFromDatabase()
            ammoTrainingHint.value = getTrainingFromDatabase()
            ammoDefault.value = getDefaultFromDatabase()
            initializeAmmos()
        }
    }

    //Suspend functions
    private suspend fun getDefaultFromDatabase(): Boolean {
        return withContext(Dispatchers.IO) {
            val value = ammoDao.get(ammoKey).defaultAmmo
            value
        }
    }
    private suspend fun getLightFromDatabase(): String {
        return withContext(Dispatchers.IO) {
            val value = ammoDao.get(ammoKey).lightAssaultRate.toString()
            value
        }
    }

    private suspend fun getMediumFromDatabse(): String {
        return withContext(Dispatchers.IO) {
            val value = ammoDao.get(ammoKey).mediumAssaultRate.toString()
            value
        }
    }

    private suspend fun getSecurityFromDatabase(): String {
        return withContext(Dispatchers.IO) {
            val value = ammoDao.get(ammoKey).securityRate.toString()
            value
        }
    }

    private suspend fun getHeavyFromDatabase(): String {
        return withContext(Dispatchers.IO) {
            val value = ammoDao.get(ammoKey).heavyAssaultRate.toString()
            value
        }
    }

    private suspend fun getSustainFromDatabase(): String {
        return withContext(Dispatchers.IO) {
            val value = ammoDao.get(ammoKey).sustainRate.toString()
            value
        }
    }

    private suspend fun getTrainingFromDatabase(): String {
        return withContext(Dispatchers.IO) {
            val value = ammoDao.get(ammoKey).trainingRate.toString()
            value
        }
    }

    private suspend fun getAmmoFromDatabase(): Ammo {
        return withContext(Dispatchers.IO) {
            val comp = ammoDao.get(ammoKey)
            comp
        }
    }

    private suspend fun getDescriptionFromDatabase(): String {
        return withContext(Dispatchers.IO) {
            val hint = ammoDao.get(ammoKey).ammoDescription
            hint
        }.toString()
    }

    private suspend fun getTypeFromDatabase(): String {
        return withContext(Dispatchers.IO) {
            val hint = ammoDao.get(ammoKey).ammoDODIC.toString()
            hint
        }
    }

    //update database
    private suspend fun update(ammo: Ammo) {
        withContext(Dispatchers.IO) {
            ammoDao.update(ammo)
        }
    }

    fun updateDodic(dodic: String) {
        uiScope.launch {
            val thisammo = ammoOld.value?: return@launch
            thisammo.ammoDODIC = dodic
            update(thisammo)
        }
    }

    fun updateSustain(desc: Int) {
        uiScope.launch {
            val thisammo = ammoOld.value?: return@launch
            thisammo.sustainRate = desc
            update(thisammo)
        }
    }

    fun updateSecurity(desc: Int) {
        uiScope.launch {
            val thisammo = ammoOld.value?: return@launch
            thisammo.securityRate = desc
            update(thisammo)
        }
    }

    fun updateTraining(desc: Int) {
        uiScope.launch {
            val thisammo = ammoOld.value?: return@launch
            thisammo.trainingRate = desc
            update(thisammo)
        }
    }

    fun updateLight(desc: Int) {
        uiScope.launch {
            val thisammo = ammoOld.value?: return@launch
            thisammo.lightAssaultRate = desc
            update(thisammo)
        }
    }

    fun updateMedium(desc: Int) {
        uiScope.launch {
            val thisammo = ammoOld.value?: return@launch
            thisammo.mediumAssaultRate = desc
            update(thisammo)
        }
    }

    fun updateHeavy(desc: Int) {
        uiScope.launch {
            val thisammo = ammoOld.value?: return@launch
            thisammo.heavyAssaultRate = desc
            update(thisammo)
        }
    }

    fun updateDesc(desc: String) {
        uiScope.launch {
            val thisammo = ammoOld.value?: return@launch
            thisammo.ammoDescription = desc
            update(thisammo)
        }
    }

    /**
     * Cancelling all jobs
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}