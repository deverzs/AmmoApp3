package com.intelliteq.fea.ammocalculator.validation

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.intelliteq.fea.ammocalculator.R
import com.intelliteq.fea.ammocalculator.adapters.ValidateAmmoAdapter
import com.intelliteq.fea.ammocalculator.adapters.ValidateComponentAdapter
import com.intelliteq.fea.ammocalculator.databinding.FragmentValidationBinding
import com.intelliteq.fea.ammocalculator.persistence.database.AmmoRoomDatabase
import com.intelliteq.fea.ammocalculator.weapon.WeaponViewModel
import kotlinx.android.synthetic.main.fragment_validation.*


/**
 * A simple [Fragment] subclass.
 * Use the [ValidationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ValidationFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentValidationBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_validation, container, false
        )

        val application = requireNotNull(this.activity).application

        val ammoDatabase = AmmoRoomDatabase.getAppDatabase(application)!!.ammoDao
        val componentDatabase = AmmoRoomDatabase.getAppDatabase(application)!!.componentDao
        val arguments = ValidationFragmentArgs.fromBundle(requireArguments())

        val viewModelFactory = ValidationViewModelFactory(
            arguments.weaponKey, componentDatabase, ammoDatabase)

        val validateViewModel = ViewModelProvider(this, viewModelFactory)
            .get(ValidationViewModel::class.java)

        binding.validationViewModel
        binding.lifecycleOwner = this


        //adapters
        val weaponAmmoValidationAdapter = ValidateAmmoAdapter()
        binding.RecyclerViewAmmoConfirmation.adapter = weaponAmmoValidationAdapter

        val componentAmmoValidationAdapter = ValidateAmmoAdapter()
        binding.RecyclerViewComponentAmmoConfirmation.adapter = componentAmmoValidationAdapter

        val componentValidationAdapter = ValidateComponentAdapter()
        binding.RecyclerViewComponentConfirmation.adapter = componentValidationAdapter



        binding.save.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(ValidationFragmentDirections.ConfirmationToCalculate(-1))
        }


        //ValidateAmmoAdapter
        validateViewModel.ammosWeapon.observe(viewLifecycleOwner, Observer {
            ammos ->
            ammos?.let {
                Log.i("Error", "Ammo weapon: $ammos")
                weaponAmmoValidationAdapter.submitList(ammos)
            }
        })

        //ValidateAmmoAdapter
        validateViewModel.ammosComp.observe(viewLifecycleOwner, Observer {
            ammos ->
            ammos?.let {
                componentAmmoValidationAdapter.submitList(ammos)
                Log.i("Error", "Ammo comp: $ammos")
                if(ammos.isEmpty()) binding.confirmCompAmmoTextView.visibility = View.GONE
            }
        })

        //ValidateComponentAdapter
        validateViewModel.components.observe(viewLifecycleOwner, Observer {
            comps ->
            comps?.let {
                componentValidationAdapter.submitList(comps)
                Log.i("Error", "Comp: $comps")
                if(comps.isEmpty()) binding.confirmComponentTextView.visibility = View.GONE
            }
        })


        //Weapon
        validateViewModel.weapon.observe(viewLifecycleOwner, Observer {
            wep ->
            wep?.let {
                binding.confWpnType.text = wep.componentTypeId
                binding.confWpnDesc.text = wep.componentDescription
                Log.i("Error", "Weapon: $wep")
            }
        })
        // Inflate the layout for this fragment
        return binding.root
    }

}
