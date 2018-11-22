package dongkyul.pospot.view.main;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TagNumResponseContainer {
    @SerializedName("result")
    private List<TagBox> result;

    public List<TagBox> getResult() {
        return result;
    }

    class TagBox {
        @SerializedName("tagNum")
        private int tagNum;

        public int getTagNum() {
            return tagNum;
        }
    }
}
