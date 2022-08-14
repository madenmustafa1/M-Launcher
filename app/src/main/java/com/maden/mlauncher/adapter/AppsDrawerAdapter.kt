package com.maden.mlauncher.adapter

import android.view.*
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.maden.mlauncher.R

import com.maden.mlauncher.model.AppInfoModel

class AppsDrawerAdapter(
    private val appsList: ArrayList<AppInfoModel>,
    private val adapterClickListener: AdapterClickListener,
    private var shaker: Boolean
) :
    RecyclerView.Adapter<AppsDrawerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View =
            inflater.inflate(R.layout.item_row_view_list, parent, false)
        return ViewHolder(view)
    }

    private val shakerHash = HashMap<String, String>()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(appsList[position]) {
            if (shaker && shakerHash[label.toString()] == null) {
                shakerHash[label.toString()] = label.toString()
                val animShake = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.shake);
                holder.img.startAnimation(animShake);
            }

            holder.textView.text = label.toString()
            val imageView: ImageView = holder.img
            imageView.setImageDrawable(icon)
            checkLastItem(holder, position)

            holder.itemView.setOnClickListener {
                adapterClickListener.onClickListener(packageName.toString(), position, icon)
            }
        }
    }

    override fun getItemCount() = appsList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView = itemView.findViewById(R.id.tv_app_name)
        var img: ImageView = itemView.findViewById(R.id.app_icon)
    }

    private fun checkLastItem(holder: ViewHolder, position: Int) {
        val hasMenuKey: Boolean = ViewConfiguration.get(holder.itemView.context).hasPermanentMenuKey()
        val hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)

        if (position == appsList.lastIndex) {
            if (hasBackKey && hasMenuKey) {

            } else {
                val params = holder.itemView.layoutParams as RecyclerView.LayoutParams
                params.bottomMargin = 140
                holder.itemView.layoutParams = params
            }
        } else {
            val params = holder.itemView.layoutParams as RecyclerView.LayoutParams
            params.bottomMargin = 0
            holder.itemView.layoutParams = params
        }
    }
}