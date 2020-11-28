package com.kulkarni.mimoh.smartstudy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Select_Quiz_Fragment extends Fragment {

    private ExpandableListView lvCategory;
    private ImageButton IBstart;
    private ArrayList<SubCategoryItem> arSubCategory;
    private ArrayList<HashMap<String, String>> parentItems;
    private ArrayList<ArrayList<HashMap<String, String>>> childItems;
    MyCategoriesExpandableListAdapter myCategoriesExpandableListAdapter;
//    private HashMap<String,String> hashMap;
    private SecurePreferences securePreferences;
    private int lastExpandedPosition = -1;
    public ArrayList<String> selected_chap;
    public String selected_sub = "";
    private ArrayList<DataItem> dataItemArrayList;

    public static final String CHECK_BOX_CHECKED_TRUE = "YES";
    public static final String CHECK_BOX_CHECKED_FALSE = "NO";
    public static final String IS_CHECKED = "is_checked";
    public static final String SUB_CATEGORY_NAME = "sub_category_name";
    public static final String CATEGORY_NAME = "category_name";
    public static final String CATEGORY_ID = "category_id";
    public static final String SUB_ID = "sub_id";
    private static final String sub_chap_url = "https://smartstudym3.000webhostapp.com/smart_study/test.php";
    private static final int timeout = 10000;

    DataItem dataItem;

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_quiz, container, false);

        lvCategory = view.findViewById(R.id.lvCategory);
        IBstart = view.findViewById(R.id.IBstart);
//        hashMap = LocalDatabase.getInstance(getContext()).getUserData();
        securePreferences = new SecurePreferences(getContext());

        arSubCategory = new ArrayList<>();
        parentItems = new ArrayList<>();
        childItems = new ArrayList<>();
        selected_chap = new ArrayList<>();
        dataItemArrayList = new ArrayList<>();

        setupReferences();

        IBstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!selected_sub.isEmpty() || !selected_chap.isEmpty()) {
                    String final_chaps = "",comma="";
                    for (int i=0;i<selected_chap.size();i++){
                        final_chaps = final_chaps + comma + selected_chap.get(i);
                        comma = ",";
                    }
                    Intent intent_quiz = new Intent(getContext(), Quiz_Activity.class);
                    intent_quiz.putExtra("sub_id", selected_sub);
                    intent_quiz.putExtra("chap_id",final_chaps);
                    intent_quiz.putExtra("test_id","0");
//                    Toast.makeText(getContext(), final_chaps, Toast.LENGTH_SHORT).show();
                    startActivity(intent_quiz);
                }
                else Toast.makeText(getContext(), "Please select chapters", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void setupReferences() {

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Getting Your Subjects And Chapters...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, sub_chap_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
//                    Toast.makeText(getContext(),response,Toast.LENGTH_SHORT).show();

                    JSONArray jsonArray = jsonObject.getJSONArray("resultarray");
                    for (int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        dataItem = new DataItem();
                        final String sub_id = jsonObject1.getString("sub_id");
                        dataItem.setCategoryId(sub_id);
                        dataItem.setCategoryName(jsonObject1.getString("sub_name"));
                        arSubCategory = new ArrayList<>();

                        JSONArray jsonArray1 = jsonObject1.getJSONArray("chapters");
                        for (int j=0;j<jsonArray1.length();j++){
                            JSONObject jsonObject2 = jsonArray1.getJSONObject(j);
                            SubCategoryItem subCategoryItem = new SubCategoryItem();
                            subCategoryItem.setCategoryId(jsonObject2.getString("chapter_id"));
                            subCategoryItem.setIsChecked(CHECK_BOX_CHECKED_FALSE);
                            subCategoryItem.setSubCategoryName(jsonObject2.getString("chapter_name"));
                            arSubCategory.add(subCategoryItem);
                        }
                        dataItem.setSubCategory(arSubCategory);
                        dataItemArrayList.add(dataItem);
                    }
                    load_list();
                    IBstart.setVisibility(View.VISIBLE);
                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                String message = "oops";
                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (error instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }
                Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("insti_id",securePreferences.getString("instiid", null));
                map.put("class",securePreferences.getString("class", null));
                map.put("medium",securePreferences.getString("medium", null));
                map.put("sub_id","");
                map.put("test_id","");
                map.put("chap_id","");
                map.put("multiple_test","");
                map.put("type","get_sub_chap");
                return map;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(timeout,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);

////        trial start
//        for (int i=1;i<=5;i++) {
//            dataItem = new DataItem();
//            final String sub_id = "1";
//            dataItem.setCategoryId(sub_id);
//            dataItem.setCategoryName("Trial subject " + String.valueOf(i));
//            arSubCategory = new ArrayList<>();
//
//            for (int j=1;j<=5;j++){
//                SubCategoryItem subCategoryItem = new SubCategoryItem();
//                subCategoryItem.setCategoryId(String.valueOf(j));
//                subCategoryItem.setIsChecked(CHECK_BOX_CHECKED_FALSE);
//                subCategoryItem.setSubCategoryName("Trial Chapter " + String.valueOf(i));
//                arSubCategory.add(subCategoryItem);
//            }
//
//            dataItem.setSubCategory(arSubCategory);
//            dataItemArrayList.add(dataItem);
//        }
//        load_list();
//        IBstart.setVisibility(View.VISIBLE);
//
////        trial end

        lvCategory.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1 && groupPosition != lastExpandedPosition)
                    lvCategory.collapseGroup(lastExpandedPosition);
                lastExpandedPosition = groupPosition;
                selected_chap.clear();
            }
        });



//        dataItem = new DataItem();
//        dataItem.setCategoryId("1");
//        dataItem.setCategoryName("Adventure");
//
//
//        for(int i = 1; i < 6; i++) {
//            SubCategoryItem subCategoryItem = new SubCategoryItem();
//            subCategoryItem.setCategoryId(String.valueOf(i));
//            subCategoryItem.setIsChecked(CHECK_BOX_CHECKED_FALSE);
//            subCategoryItem.setSubCategoryName("Adventure: "+i);
//            arSubCategory.add(subCategoryItem);
//        }
//        dataItem.setSubCategory(arSubCategory);
//        arCategory.add(dataItem);
//
//
//        dataItem = new DataItem();
//        dataItem.setCategoryId("2");
//        dataItem.setCategoryName("Art");
//        arSubCategory = new ArrayList<>();
//
//        for(int i = 6; i < 11; i++) {
//            SubCategoryItem subCategoryItem = new SubCategoryItem();
//            subCategoryItem.setCategoryId(String.valueOf(i));
//            subCategoryItem.setIsChecked(CHECK_BOX_CHECKED_FALSE);
//            subCategoryItem.setSubCategoryName("Art: "+i);
//            arSubCategory.add(subCategoryItem);
//        }
//        dataItem.setSubCategory(arSubCategory);
//        arCategory.add(dataItem);
//
//        dataItem = new DataItem();
//        dataItem.setCategoryId("3");
//        dataItem.setCategoryName("Cooking");
//        arSubCategory = new ArrayList<>();
//
//        for(int i = 11; i < 16; i++) {
//            SubCategoryItem subCategoryItem = new SubCategoryItem();
//            subCategoryItem.setCategoryId(String.valueOf(i));
//            subCategoryItem.setIsChecked(CHECK_BOX_CHECKED_FALSE);
//            subCategoryItem.setSubCategoryName("Cooking: "+i);
//            arSubCategory.add(subCategoryItem);
//        }
//
//        dataItem.setSubCategory(arSubCategory);
//        arCategory.add(dataItem);
//
//        load_list();


    }

    private void load_list(){
        for (DataItem data : dataItemArrayList) {
            ArrayList<HashMap<String, String>> childArrayList = new ArrayList<>();
            HashMap<String, String> mapParent = new HashMap<>();

            mapParent.put(CATEGORY_ID, data.getCategoryId());
            mapParent.put(CATEGORY_NAME, data.getCategoryName());

            int countIsChecked = 0;
            for (SubCategoryItem subCategoryItem : data.getSubCategory()) {

                HashMap<String, String> mapChild = new HashMap<>();
                mapChild.put(SUB_ID, subCategoryItem.getCategoryId());
                mapChild.put(SUB_CATEGORY_NAME, subCategoryItem.getSubCategoryName());
                mapChild.put(CATEGORY_ID, subCategoryItem.getCategoryId());
                mapChild.put(IS_CHECKED, subCategoryItem.getIsChecked());

                if (subCategoryItem.getIsChecked().equalsIgnoreCase(CHECK_BOX_CHECKED_TRUE)) {
                    countIsChecked++;
                }
                childArrayList.add(mapChild);
            }

            if (countIsChecked == data.getSubCategory().size())
                data.setIsChecked(CHECK_BOX_CHECKED_TRUE);
            else data.setIsChecked(CHECK_BOX_CHECKED_FALSE);

            mapParent.put(IS_CHECKED, data.getIsChecked());
            childItems.add(childArrayList);
            parentItems.add(mapParent);
        }
        myCategoriesExpandableListAdapter = new MyCategoriesExpandableListAdapter(parentItems, childItems);
        lvCategory.setAdapter(myCategoriesExpandableListAdapter);
    }

    public class MyCategoriesExpandableListAdapter extends BaseExpandableListAdapter {

        private final ArrayList<ArrayList<HashMap<String, String>>> childItems;
        private ArrayList<HashMap<String, String>> parentItems;
        private LayoutInflater inflater;
        private HashMap<String, String> child;
        private int count = 0;
//        private boolean isFromMyCategoriesFragment;

        private MyCategoriesExpandableListAdapter(ArrayList<HashMap<String, String>> parentItems,
                                                  ArrayList<ArrayList<HashMap<String, String>>> childItems) {
            this.parentItems = parentItems;
            this.childItems = childItems;
//            this.isFromMyCategoriesFragment = isFromMyCategoriesFragment;
            inflater = (LayoutInflater) Objects.requireNonNull(getContext()).getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getGroupCount() {
            return parentItems.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return (childItems.get(groupPosition)).size();
        }

        @Override
        public Object getGroup(int i) {
            return null;
        }

        @Override
        public Object getChild(int i, int i1) {
            return null;
        }

        @Override
        public long getGroupId(int i) {
            return 0;
        }

        @Override
        public long getChildId(int i, int i1) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(final int groupPosition, final boolean b, View convertView, ViewGroup viewGroup) {
            final ViewHolderParent viewHolderParent;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.notes_group_list, null);
                viewHolderParent = new ViewHolderParent();

                viewHolderParent.tvMainCategoryName = convertView.findViewById(R.id.tvMainCategoryName);
                viewHolderParent.cbMainCategory = convertView.findViewById(R.id.cbMainCategory);
                viewHolderParent.ivCategory = convertView.findViewById(R.id.ivCategory);
                convertView.setTag(viewHolderParent);
            } else {
                viewHolderParent = (ViewHolderParent) convertView.getTag();
            }

            if (parentItems.get(groupPosition).get(IS_CHECKED).equalsIgnoreCase(CHECK_BOX_CHECKED_TRUE)) {
                viewHolderParent.cbMainCategory.setChecked(true);
                notifyDataSetChanged();

            } else {
                viewHolderParent.cbMainCategory.setChecked(false);
                notifyDataSetChanged();
            }

            viewHolderParent.cbMainCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (viewHolderParent.cbMainCategory.isChecked()) {
                        for (int i = 0;i < parentItems.size();i++){
                            parentItems.get(i).put(IS_CHECKED,CHECK_BOX_CHECKED_FALSE);
                            for (int j = 0; j < childItems.get(i).size(); j++) {
                                childItems.get(i).get(j).put(IS_CHECKED, CHECK_BOX_CHECKED_FALSE);
                            }
                        }
                        selected_chap.clear();
                        parentItems.get(groupPosition).put(IS_CHECKED, CHECK_BOX_CHECKED_TRUE);
                        selected_sub = dataItemArrayList.get(groupPosition).getCategoryId();
//                        Toast.makeText(getContext(),dataItemArrayList.get(groupPosition).getCategoryId(),Toast.LENGTH_SHORT).show();

                        for (int i = 0; i < childItems.get(groupPosition).size(); i++) {
                            selected_chap.add(dataItemArrayList.get(groupPosition).getSubCategory().get(i).getCategoryId());
                            childItems.get(groupPosition).get(i).put(IS_CHECKED, CHECK_BOX_CHECKED_TRUE);
                        }
                        notifyDataSetChanged();

                    }
                    else {
                        parentItems.get(groupPosition).put(IS_CHECKED, CHECK_BOX_CHECKED_FALSE);
                        selected_sub = "";
                        selected_chap.clear();
                        for (int i = 0; i < childItems.get(groupPosition).size(); i++) {
                            childItems.get(groupPosition).get(i).put(IS_CHECKED,CHECK_BOX_CHECKED_FALSE);
                        }
                        notifyDataSetChanged();
                    }
                }
            });

            viewHolderParent.tvMainCategoryName.setText(parentItems.get(groupPosition).get(CATEGORY_NAME));

            return convertView;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, final boolean b, View convertView, ViewGroup viewGroup) {

            final ViewHolderChild viewHolderChild;
            child = childItems.get(groupPosition).get(childPosition);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.notes_child_list, null);
                viewHolderChild = new ViewHolderChild();

                viewHolderChild.tvSubCategoryName = convertView.findViewById(R.id.tvSubCategoryName);
                viewHolderChild.cbSubCategory = convertView.findViewById(R.id.cbSubCategory);
                viewHolderChild.viewDivider = convertView.findViewById(R.id.viewDivider);
                convertView.setTag(viewHolderChild);
            } else {
                viewHolderChild = (ViewHolderChild) convertView.getTag();
            }

            if (childItems.get(groupPosition).get(childPosition).get(IS_CHECKED).equalsIgnoreCase(CHECK_BOX_CHECKED_TRUE)) {
                viewHolderChild.cbSubCategory.setChecked(true);
                notifyDataSetChanged();
            } else {
                viewHolderChild.cbSubCategory.setChecked(false);
                notifyDataSetChanged();
            }

            viewHolderChild.tvSubCategoryName.setText(child.get(SUB_CATEGORY_NAME));
            viewHolderChild.cbSubCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (viewHolderChild.cbSubCategory.isChecked()) {
                        for (int i = 0; i<parentItems.get(groupPosition).size();i++){
                            if (i != groupPosition){
                                parentItems.get(i).put(IS_CHECKED,CHECK_BOX_CHECKED_FALSE);
                                for (int j = 0; j < childItems.get(i).size(); j++) {
                                    childItems.get(i).get(j).put(IS_CHECKED, CHECK_BOX_CHECKED_FALSE);
                                }
                            }
                        }

                        count = 0;
                        childItems.get(groupPosition).get(childPosition).put(IS_CHECKED, CHECK_BOX_CHECKED_TRUE);
                        selected_sub = dataItemArrayList.get(groupPosition).getCategoryId();
                        selected_chap.add(dataItemArrayList.get(groupPosition).getSubCategory().get(childPosition).getCategoryId());
//                        Toast.makeText(getContext(),dataItemArrayList.get(groupPosition).getSubCategory().get(childPosition).getCategoryId(),Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                    } else {
                        count = 0;
                        selected_chap.remove(dataItemArrayList.get(groupPosition).getSubCategory().get(childPosition).getCategoryId());
                        childItems.get(groupPosition).get(childPosition).put(IS_CHECKED, CHECK_BOX_CHECKED_FALSE);
                        notifyDataSetChanged();
                    }

                    for (int i = 0; i < childItems.get(groupPosition).size(); i++) {
                        if (childItems.get(groupPosition).get(i).get(IS_CHECKED).equalsIgnoreCase(CHECK_BOX_CHECKED_TRUE)) {
                            count++;
                        }
                    }
                    if (count == childItems.get(groupPosition).size()) {
                        parentItems.get(groupPosition).put(IS_CHECKED, CHECK_BOX_CHECKED_TRUE);
                        selected_sub = dataItemArrayList.get(groupPosition).getCategoryId();
                        notifyDataSetChanged();
                    } else {
                        parentItems.get(groupPosition).put(IS_CHECKED, CHECK_BOX_CHECKED_FALSE);
                        notifyDataSetChanged();
                    }
                }
            });

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return false;
        }
        @Override
        public void onGroupCollapsed(int groupPosition) {
            super.onGroupCollapsed(groupPosition);
        }

        @Override
        public void onGroupExpanded(int groupPosition) {
            super.onGroupExpanded(groupPosition);
        }

        private class ViewHolderParent {

            TextView tvMainCategoryName;
            CheckBox cbMainCategory;
            ImageView ivCategory;
        }

        private class ViewHolderChild {

            TextView tvSubCategoryName;
            CheckBox cbSubCategory;
            View viewDivider;
        }
    }

    private class DataItem {

        private String categoryId;
        private String categoryName;
        private String isChecked = "NO";
        private List<SubCategoryItem> subCategory;

        private String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        private String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        private String getIsChecked() {
            return isChecked;
        }

        private void setIsChecked(String isChecked) {
            this.isChecked = isChecked;
        }

        private List<SubCategoryItem> getSubCategory() {
            return subCategory;
        }

        private void setSubCategory(List<SubCategoryItem> subCategory) {
            this.subCategory = subCategory;
        }
    }

    private class SubCategoryItem {

        private String categoryId;
        private String subCategoryName;
        private String isChecked;

        private String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        private String getSubCategoryName() {
            return subCategoryName;
        }

        public void setSubCategoryName(String subCategoryName) {
            this.subCategoryName = subCategoryName;
        }

        private String getIsChecked() {
            return isChecked;
        }

        public void setIsChecked(String isChecked) {
            this.isChecked = isChecked;
        }
    }
}