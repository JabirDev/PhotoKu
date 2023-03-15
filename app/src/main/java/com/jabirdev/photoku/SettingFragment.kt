package com.jabirdev.photoku

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.jabirdev.photoku.adapter.SettingAdapter
import com.jabirdev.photoku.databinding.FragmentSettingBinding
import com.jabirdev.photoku.vm.SettingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding
    private val settingViewModel: SettingViewModel by viewModels()
    private val settingAdapter = SettingAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding?.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.rvSetting?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = settingAdapter
        }

        settingViewModel.menuList.observe(viewLifecycleOwner){
            settingAdapter.setData(it)
        }

        settingViewModel.getMenuList(requireContext())

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}