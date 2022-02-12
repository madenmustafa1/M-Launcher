package com.maden.mlauncher.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.maden.mlauncher.databinding.FragmentMainPageBinding
import com.maden.mlauncher.adapter.AppsDrawerAdapter
import com.maden.mlauncher.util.DateUtil


class MainPageFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    var recyclerView: RecyclerView? = null
    var adapter: RecyclerView.Adapter<*>? = null
    var layoutManager: RecyclerView.LayoutManager? = null

    private var _binding: FragmentMainPageBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initButton()


    }

    private fun initButton(){
        binding.systemHour.text = DateUtil.getSystemHourAndMin()
        binding.systemDate.text = DateUtil.getSystemDate()

        initRecyclerView()
    }


    private fun initRecyclerView() {
        recyclerView = binding.appDrawerRecylerView

        adapter = AppsDrawerAdapter(requireContext());

        layoutManager = LinearLayoutManager(requireContext());

        recyclerView!!.layoutManager = layoutManager;
        recyclerView!!.adapter = adapter;
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}