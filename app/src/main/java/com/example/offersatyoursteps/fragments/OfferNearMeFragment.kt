package com.example.offersatyoursteps.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.offersatyoursteps.R
import com.example.offersatyoursteps.adapters.OfferAdapter
import com.example.offersatyoursteps.adapters.ProductSubcategoryAdapter
import com.example.offersatyoursteps.utilities.OfferConstants
import com.example.offersatyoursteps.databinding.FragmentOfferNearMeBinding
import com.example.offersatyoursteps.models.UserModel
import com.example.offersatyoursteps.services.Dataservices
import com.example.offersatyoursteps.utilities.SPAN_COUNT
import com.example.offersatyoursteps.utilities.USER_INFO


class OfferNearMeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1 : String? = null
    private var param2 : String? = null
    
    private lateinit var userModel : UserModel
    
    private lateinit var binding : FragmentOfferNearMeBinding
    
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userModel = it?.getParcelable<UserModel>(USER_INFO)!!
        }
    }
    
    override fun onCreateView(
        inflater : LayoutInflater, container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View? {
        // Inflate the layout for this fragment
        binding = FragmentOfferNearMeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
    
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OfferNearMeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(userModel : UserModel) =
            OfferNearMeFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(USER_INFO, userModel)
                }
            }
    }
    
    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val offerList = OfferConstants.getOfferData()
        val itemAdapter = OfferAdapter(this.requireContext(), Dataservices.getProducts(userModel.prodSubcategory))
        val offerRecycleView = binding.offerNearMeRecycleView
        offerRecycleView.layoutManager = GridLayoutManager(context, SPAN_COUNT)
        offerRecycleView.adapter = itemAdapter
    }
}