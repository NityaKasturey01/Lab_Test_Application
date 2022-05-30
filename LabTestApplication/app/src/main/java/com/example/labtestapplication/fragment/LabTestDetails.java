package com.example.labtestapplication.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.labtestapplication.BookTestAct;
import com.example.labtestapplication.R;
import com.squareup.picasso.Picasso;

public class LabTestDetails extends Fragment {

    private AppCompatTextView testName, collection, parameter, field, age, description, prereq, pathologyName, pathologyContact, cost, report;
    private AppCompatImageView testImage;
    private AppCompatButton bookTest;
    View view;

    public LabTestDetails() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_lab_test_details, container, false);
        getActivity().setTitle("Test Details");
        bookTest = view.findViewById(R.id.bookTestButton);
        testImage = (AppCompatImageView)view.findViewById(R.id.testImageDisplay);
        testName = (AppCompatTextView) view.findViewById(R.id.testNameDisplay);
        collection = view.findViewById(R.id.collectionDisplay);
        parameter = view.findViewById(R.id.parametersDisplay);
        field = view.findViewById(R.id.fieldDisplay);
        age = view.findViewById(R.id.ageDisplay);
        description = view.findViewById(R.id.descriptionDisplay);
        prereq = view.findViewById(R.id.prereqDisplay);
        pathologyName = view.findViewById(R.id.pathologyDisplay);
        pathologyContact = view.findViewById(R.id.pathologyContactDisplay);
        cost = view.findViewById(R.id.costDisplay);
        report = view.findViewById(R.id.reportDisplay);

        com.example.labtestapplication.fragment.LabTests labTests = new LabTests();
        //age.setText(labTests.getAge_limit());
            Bundle bundle = this.getArguments();
            Picasso.with(getContext()).load(String.valueOf(bundle.getCharSequence("image"))).into(testImage);
            testName.setText(bundle.getCharSequence("testname"));
            collection.setText(bundle.getCharSequence("collection"));
            parameter.setText(bundle.getCharSequence("parameter"));
            field.setText(bundle.getCharSequence("field"));
            age.setText(bundle.getCharSequence("age"));
            description.setText(bundle.getCharSequence("description"));
            prereq.setText(bundle.getCharSequence("prereq"));
            pathologyName.setText(bundle.getCharSequence("pathloc"));
            pathologyContact.setText(bundle.getCharSequence("pathcontact"));
            cost.setText(bundle.getCharSequence("cost"));
            report.setText(bundle.getCharSequence("report"));

            bookTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), BookTestAct.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

        return view;
    }
}