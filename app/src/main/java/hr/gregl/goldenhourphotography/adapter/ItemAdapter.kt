package hr.gregl.goldenhourphotography.adapter


import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hr.gregl.goldenhourphotography.R
import hr.gregl.goldenhourphotography.TIME_PROVIDER_CONTENT_URI
import hr.gregl.goldenhourphotography.model.Item


class ItemAdapter(
    private val context: Context,
    private val items: MutableList<Item>
) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    private var detailsVisibilityMap = mutableMapOf<Long, Boolean>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDate = itemView.findViewById<TextView>(R.id.tvDate)
        private val tvGoldenHour = itemView.findViewById<TextView>(R.id.tvGoldenHour)
        private val additionalDetailsLayout = itemView.findViewById<LinearLayout>(R.id.additionalDetailsLayout)
        private val tvTimeZone = itemView.findViewById<TextView>(R.id.tvTimeZone)
        private val tvSunrise = itemView.findViewById<TextView>(R.id.tvSunrise)
        private val tvSunset = itemView.findViewById<TextView>(R.id.tvSunset)
        private val tvFirstLight = itemView.findViewById<TextView>(R.id.tvFirstLight)
        private val tvLastLight = itemView.findViewById<TextView>(R.id.tvLastLight)
        private val tvDawn = itemView.findViewById<TextView>(R.id.tvDawn)
        private val tvDusk = itemView.findViewById<TextView>(R.id.tvDusk)


        @SuppressLint("SetTextI18n")
        fun bind(item: Item, isDetailsVisible: Boolean) {
            tvDate.text = "Date: ${item.date}"
            tvGoldenHour.text = "Golden hour: ${item.goldenHour}"
            additionalDetailsLayout.visibility = if (isDetailsVisible) View.VISIBLE else View.GONE
            tvTimeZone.text = "Time zone: ${item.timezone.substringAfter("/")}"
            tvSunrise.text = "Sunrise: ${item.sunrise}"
            tvSunset.text = "Sunset: ${item.sunset}"
            tvFirstLight.text = "First light: ${item.firstLight}"
            tvLastLight.text = "Last light: ${item.lastLight}"
            tvDawn.text = "Dawn: ${item.dawn}"
            tvDusk.text = "Dusk: ${item.dusk}"

            itemView.setOnClickListener {
                additionalDetailsLayout.visibility = if (additionalDetailsLayout.visibility == View.GONE) View.VISIBLE else View.GONE
            }
        }

        fun isDetailsVisible(): Boolean {
            return additionalDetailsLayout.visibility == View.VISIBLE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item, parent, false))
    }


    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        // Get the current visibility state for this item, defaulting to false
        val isDetailsVisible = detailsVisibilityMap[item._id] ?: false

        holder.itemView.setOnLongClickListener {
            // Remove item from database
            item._id?.let { id ->
                val uri = ContentUris.withAppendedId(TIME_PROVIDER_CONTENT_URI, id)
                context.contentResolver.delete(uri, null, null)
            }

            // Remove item from the list and notify the adapter
            items.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, items.size - position)
            true
        }
        holder.bind(item, isDetailsVisible)
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        val item = items.getOrNull(holder.adapterPosition)
        item?._id?.let {
            detailsVisibilityMap[it] = holder.isDetailsVisible()
        }
    }
}