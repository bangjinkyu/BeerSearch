package com.room.beersearch.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.room.beersearch.R
import com.room.beersearch.databinding.FragmentDetailBinding
import com.room.beersearch.model.BeerSearch

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class DetailFragment : Fragment() {

    private  var _binding: FragmentDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data  = arguments?.getParcelable<BeerSearch>("beeritem")
        val keyword  = arguments?.getString("keyword")
        binding.mydata = data
        binding.buttonSecond.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("keyword",keyword)
            findNavController().navigate(R.id.action_DetailFragment_to_ListFragment, bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}