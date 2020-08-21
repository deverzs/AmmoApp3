package com.intelliteq.fea.ammocalculator.weaponAmmo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.intelliteq.fea.ammocalculator.R
import com.intelliteq.fea.ammocalculator.databinding.FragmentWeaponAmmoBinding
import com.intelliteq.fea.ammocalculator.persistence.database.AmmoRoomDatabase


/**
 * Fragment class for WeaponAmmo
 */
class WeaponAmmoFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //binding variable and inflating the fragment
        val binding: FragmentWeaponAmmoBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_weapon_ammo, container, false
        )

        //getting the application, arguments set and database
        val application = requireNotNull(this.activity).application
        val arguments = WeaponAmmoFragmentArgs.fromBundle(arguments)
        val dataSource = AmmoRoomDatabase.getAppDatabase(application)!!.weaponAmmoDao

        //creating a view model using the factory
        val viewModelFactory = WeaponAmmoViewModelFactory(arguments.weaponKey, dataSource)
        val weaponAmmoViewModel =
            ViewModelProvider(this, viewModelFactory)
                .get(WeaponAmmoViewModel::class.java)

        //setting the binding values
        binding.weaponAmmoViewModel = weaponAmmoViewModel
        binding.lifecycleOwner = this

        //setting the navigation paths for the buttons
        //checking if all edit texts are valid inputs
        weaponAmmoViewModel.checkStatusOfInputs.observe(
            viewLifecycleOwner,
            Observer { status ->
                status?.let {
                    if (!status) Toast.makeText(activity, "All fields need filled", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        )

        //navigate to component screen
        weaponAmmoViewModel.navigateToInputComponent.observe(
            viewLifecycleOwner,
            Observer { weaponAmmo ->
                weaponAmmo?.let {
                    this.findNavController()
                        .navigate(WeaponAmmoFragmentDirections.AmmoInputToComponentInput(weaponAmmo.weaponId))
                    weaponAmmoViewModel.doneNavigatingToComp()
                }
            })

        //navigate back to weaponAmmo input
        weaponAmmoViewModel.navigateToAddAnotherAmmo.observe(
            viewLifecycleOwner,
            Observer { weaponAmmo ->
                weaponAmmo?.let {
                    this.findNavController()
                        .navigate(WeaponAmmoFragmentDirections.WeaponAmmoInputToSelf(weaponAmmo.weaponId))
                    weaponAmmoViewModel.doneNavigatingToAmmo()
                }
            })

        //navigate to confirmation screen
        weaponAmmoViewModel.navigateToConfirmation.observe(
            viewLifecycleOwner,
            Observer {
                this.findNavController()
                    .navigate(WeaponAmmoFragmentDirections.ActionWeaponAmmoInputToVerify(it))
            }
        )

        // Inflate the layout for this fragment
        return binding.root
    }


}