package Page2_X;

import java.util.ArrayList;

public interface Page2_X_Interface {
    void onClick(double x, double y, String name);
    void make_db(String countId, String name, String image, String type);
    void make_dialog();
    void onData(ArrayList<Page2_X_CategoryBottom.Category_item> text);
    void delete_db(String countId);
}
