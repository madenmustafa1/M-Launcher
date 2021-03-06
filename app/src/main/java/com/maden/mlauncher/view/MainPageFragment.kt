package com.maden.mlauncher.view


import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.maden.mlauncher.R
import com.maden.mlauncher.adapter.AppsDrawerAdapter
import com.maden.mlauncher.databinding.FragmentMainPageBinding
import com.maden.mlauncher.model.AppInfoModel
import com.maden.mlauncher.util.AppUtil
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

    private var recyclerView: RecyclerView? = null
    private var adapter: RecyclerView.Adapter<*>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var appInfoModelList: ArrayList<AppInfoModel> = arrayListOf()

    private var _binding: FragmentMainPageBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        transparentStatusBar()
        initButton()

        setUpApps().also { initApps() }
    }

    private fun initButton() {
        binding.systemHour.text = DateUtil.getSystemHourAndMin()
        binding.systemDate.text = DateUtil.getSystemDate()

        initRecyclerView()
    }

    private fun initRecyclerView() {
        recyclerView = binding.appDrawerRecylerView
        adapter = AppsDrawerAdapter(requireContext(), requireActivity());
        layoutManager = LinearLayoutManager(requireContext());

        recyclerView?.let {
            it.layoutManager = layoutManager;
            it.adapter = adapter;
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun transparentStatusBar() {
        val window: Window = requireActivity().window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.transparent)
    }

    private fun setUpApps() {
        val pManager: PackageManager = requireContext().packageManager
        val i = Intent(Intent.ACTION_MAIN, null)
        i.addCategory(Intent.CATEGORY_LAUNCHER)
        val allApps = pManager.queryIntentActivities(i, 0)
        for (ri in allApps) {
            if (ri.activityInfo.packageName == AppUtil.firstApp ||
                ri.activityInfo.packageName == AppUtil.secondApp ||
                ri.activityInfo.packageName == AppUtil.thirdApp
            ) {
                appInfoModelList.add(AppInfoModel(
                    label = ri.loadLabel(pManager),
                    packageName = ri.activityInfo.packageName,
                    icon = ri.activityInfo.loadIcon(pManager)
                ))
            }
        }
    }

    private fun initApps(){
        setFirstApp()
        setSecondApp()
        setThirdApp()
    }

    private fun setFirstApp() {
        for (i in appInfoModelList) {
            if (i.packageName == AppUtil.firstApp) {
                with(binding.appFirst) {
                    setImageDrawable(i.icon)
                    setOnClickListener {
                        goSelectedApp(i.packageName.toString())
                    }
                }
            }
        }
    }

    private fun setSecondApp() {
        for (i in appInfoModelList) {
            if (i.packageName == AppUtil.secondApp) {
                with(binding.appSecond) {
                    setImageDrawable(i.icon)
                    setOnClickListener {
                        goSelectedApp(i.packageName.toString())
                    }
                }
            }
        }
    }

    private fun setThirdApp() {
        for (i in appInfoModelList) {
            if (i.packageName == AppUtil.thirdApp) {
                with(binding.appThird) {
                    setImageDrawable(i.icon)
                    setOnClickListener {
                        goSelectedApp(i.packageName.toString())
                    }
                }
            }
        }
    }

    private fun goSelectedApp(packageName: String){
        val launchIntent: Intent? =
            requireContext().packageManager.getLaunchIntentForPackage(packageName)
        if (launchIntent != null) {
            requireActivity().startActivity(
                launchIntent,
                null
            )

            requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }
}