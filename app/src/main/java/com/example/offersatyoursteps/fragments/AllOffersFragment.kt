package com.example.offersatyoursteps.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.offersatyoursteps.R
import com.example.offersatyoursteps.adapters.OfferAdapter
import com.example.offersatyoursteps.utilities.OfferConstants
import com.example.offersatyoursteps.databinding.FragmentAllOffersBinding
import com.example.offersatyoursteps.models.OfferProductDetails
import com.example.offersatyoursteps.models.UserModel
import com.example.offersatyoursteps.services.DatabaseServices
import com.example.offersatyoursteps.utilities.SPAN_COUNT
import com.example.offersatyoursteps.utilities.USER_INFO
import com.google.firebase.auth.FirebaseAuth


class AllOffersFragment : Fragment() {
    // TODO: Rename and change types of parameters
    
    private lateinit var binding : FragmentAllOffersBinding
    private var productList : MutableList<OfferProductDetails> = mutableListOf()
    
    private var userModel = UserModel("", "", "", "", "", "","")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userModel = it?.getParcelable<UserModel>(USER_INFO)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAllOffersBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(userModel : UserModel) =
            AllOffersFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(USER_INFO,userModel)
                }
            }
    }
    
    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        DatabaseServices.getProductDetailsRecord("Product_Details",productList){
                isGetComplete ->
            if(isGetComplete){
                val itemAdapter = OfferAdapter(this.requireContext(), productList)
                val offerRecycleView = binding.offerAllRecycleView
                offerRecycleView.layoutManager = GridLayoutManager(context, SPAN_COUNT)
                offerRecycleView.adapter = itemAdapter
            } else{
                Log.d("DEBUG", "OfferNearMe - no record returned")
            }
        }
    }
}