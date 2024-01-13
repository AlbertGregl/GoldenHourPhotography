package hr.gregl.goldenhourphotography.adapter


import android.content.ContentUris
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hr.gregl.goldenhourphotography.R
import hr.gregl.goldenhourphotography.TIME_PROVIDER_CONTENT_URI
import hr.gregl.goldenhourphotography.model.Item
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import java.io.File

class ItemAdapter(
    private val context: Context,
    private val items: MutableList<Item>
) : RecyclerView.Adapter<ItemAdapter.ViewHolder>(){
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDate = itemView.findViewById<TextView>(R.id.tvDate)
        private  val tvTimeZone = itemView.findViewById<TextView>(R.id.tvTimeZone)
        private val tvSunrise = itemView.findViewById<TextView>(R.id.tvSunrise)
        private val tvSunset = itemView.findViewById<TextView>(R.id.tvSunset)
        private val tvFirstLight = itemView.findViewById<TextView>(R.id.tvFirstLight)
        private val tvLastLight = itemView.findViewById<TextView>(R.id.tvLastLight)
        private val tvDawn = itemView.findViewById<TextView>(R.id.tvDawn)
        private val tvDusk = itemView.findViewById<TextView>(R.id.tvDusk)
        private val tvGoldenHour = itemView.findViewById<TextView>(R.id.tvGoldenHour)

        fun bind(item: Item) {
            tvDate.text = "Date: ${item.date}"
            tvTimeZone.text = "Time zone: ${item.timezone.substringAfter("/")}"
            tvSunrise.text = "Sunrise: ${item.sunrise}"
            tvSunset.text = "Sunset: ${item.sunset}"
            tvFirstLight.text = "First light: ${item.firstLight}"
            tvLastLight.text = "Last light: ${item.lastLight}"
            tvDawn.text = "Dawn: ${item.dawn}"
            tvDusk.text = "Dusk: ${item.dusk}"
            tvGoldenHour.text = "Golden hour: ${item.goldenHour}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.item, parent, false)
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemView.setOnLongClickListener {
            // remove item from database
            val item = items[position]
            val uri =
                item._id?.let { it1 -> ContentUris.withAppendedId(TIME_PROVIDER_CONTENT_URI, it1) }
            if (uri != null) {
                context.contentResolver.delete(uri, null, null)

            }
            // remove item from list
            items.removeAt(position)
            notifyItemRemoved(position)
            true
        }

        holder.itemView.setOnClickListener {
            // TODO Define what happens on click
        }

        holder.bind(items[position])
    }
}