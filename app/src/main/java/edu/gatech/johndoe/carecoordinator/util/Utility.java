package edu.gatech.johndoe.carecoordinator.util;

import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.client.IGenericClient;
import edu.gatech.johndoe.carecoordinator.community.UI.CommunityAdapter;
import edu.gatech.johndoe.carecoordinator.community.UI.CommunityDetailFragment;
import edu.gatech.johndoe.carecoordinator.ContentListFragment;
import edu.gatech.johndoe.carecoordinator.MainActivity;
import edu.gatech.johndoe.carecoordinator.R;
import edu.gatech.johndoe.carecoordinator.care_plan.UI.CarePlanDetailFragment;
import edu.gatech.johndoe.carecoordinator.care_plan.UI.CarePlanListAdapter;
import edu.gatech.johndoe.carecoordinator.UnselectedFragment;
import edu.gatech.johndoe.carecoordinator.community.Community;
import edu.gatech.johndoe.carecoordinator.community.Nutritionist;
import edu.gatech.johndoe.carecoordinator.community.Physical;
import edu.gatech.johndoe.carecoordinator.community.Restaurant;
import edu.gatech.johndoe.carecoordinator.care_plan.CarePlan;
import edu.gatech.johndoe.carecoordinator.patient.UI.PatientAdapter;
import edu.gatech.johndoe.carecoordinator.patient.UI.PatientDetailFragment;

public class Utility {
    private static final String SERVER_BASE = "http://52.72.172.54:8080/fhir/baseDstu2";
    private static final String MAPS_API_KEY = "AIzaSyADCKXv1I_2Z0zAQ8CPMs-32YmhKGtkYBY";
    private static final FhirContext ctx = FhirContext.forDstu2();
    private static final IGenericClient client = ctx.newRestfulGenericClient(SERVER_BASE);
    public static final Firebase CARE_PLANS_REF =
            new Firebase("https://cdccoordinator2.firebaseio.com/care_plans");
    public static final Firebase PATIENTS_REF =
            new Firebase("https://cdccoordinator2.firebaseio.com/patients");
    public static final Firebase COMMUNITES_REF =
            new Firebase("https://cdccoordinator2.firebaseio.com/community_resources");
    public static final Firebase PHYSICAL_REF =
            new Firebase("https://cdccoordinator2.firebaseio.com/community_resources/physical");
    public static final Firebase NUTRITIONIST_REF =
            new Firebase("https://cdccoordinator2.firebaseio.com/community_resources/nutritionist");
    public static final Firebase RESTAURANT_REF =
            new Firebase("https://cdccoordinator2.firebaseio.com/community_resources/restaurant");
    public static final Firebase INCOMING_REF =
            new Firebase("https://cdccoordinator2.firebaseio.com/incoming");
    public static List<CarePlan> carePlan_list = new ArrayList<>();
    public static List<edu.gatech.johndoe.carecoordinator.patient.Patient> patient_list = new ArrayList<>();
    public static List<Community> community_list = new ArrayList<>();
    public static final String UPDATE_MESSAGE = "Data Updated.";

    // previously dummyDataGenerator()
    public static void populateDatabase() {
        Random random = new Random();
        // Generating Fake Communities
//        List<Community> communities = new ArrayList<>();
//        for (int i = 1; i <= 30; i++) {
//            communities.add(new Community(String.valueOf(i), "Community" + i, "450 Madison Court, Deactur, GA 30030", "(678) 148 - 4606", "johndoe@gmail.com", "No Information"));
//        }
        // Generating Fake Patients and their Referrals
        int j, ehrID = 1;
        for (int i = 1; i < 50; i++) {
            ca.uhn.fhir.model.dstu2.resource.Patient p = getFHIRPatientByID(i);
            if (p != null) {
                edu.gatech.johndoe.carecoordinator.patient.Patient patient = new edu.gatech.johndoe.carecoordinator.patient.Patient(p);
//                System.out.println(patient);
//                for (j = ehrID; j <= ehrID + random.nextInt(15); j++) {
//                    CarePlan ehr = new CarePlan(String.valueOf(j), patient.getId(), "CarePlan " + j, "None", j % 2 == 0, new Date());
//                    patient.addCarePlan(ehr);
//                    saveCarePlan(ehr);
//                }
//                ehrID = j;
//                Community community = communities.get((int) (Math.random()* 10));
//                community.addPatient(patient);
//                patient.addCommunity(community);
//                ArrayList<String> d = new ArrayList<>();
//                d.add("1");
//                patient.setReferralList(d);
//                patient.setCommunityList(d);
                savePatient(patient);
            }
        }
        //Saving Fake Communities
//        for (Community c : communities)
//            saveCommunityResource(c);
    }

    public static void saveCarePlan(CarePlan carePlan) {
        Firebase ref = CARE_PLANS_REF.child(carePlan.getId());
        ref.setValue(carePlan);
    }

    public static void savePatient(edu.gatech.johndoe.carecoordinator.patient.Patient p) {
        Firebase ref = PATIENTS_REF.child(p.getId());
        ref.child("active").setValue(p.isActive());
        ref.child("address_first").setValue(p.getAddress_first());
        ref.child("address_second").setValue(p.getAddress_second());
        ref.child("age").setValue(p.getAge());
        ref.child("birth_date").setValue(p.getBirth_date());
        ref.child("communityList").setValue(p.getCommunityList());
        ref.child("dateOfimport").setValue(p.getDateOfimport());
        ref.child("ehrList").setValue(p.getReferralList());
        ref.child("email").setValue(p.getEmail());
        ref.child("first_name").setValue(p.getFirst_name());
        ref.child("formatted_birth_date").setValue(p.getFormatted_birth_date());
        ref.child("full_name_first").setValue(p.getFull_name_first());
        ref.child("full_name_last").setValue(p.getFull_name_last());
        ref.child("gender").setValue(p.getGender());
        ref.child("id").setValue(p.getId());
        ref.child("lastUpdated").setValue(p.getLastUpdated());
        ref.child("last_name").setValue(p.getLast_name());
        ref.child("phoneNumber").setValue(p.getPhoneNumber());
        ref.child("type").setValue(p.getType());
    }
    
    public static void saveCommunityResource(Community community) {
        Firebase ref = COMMUNITES_REF.child(community.getId());
        ref.setValue(community);
    }

    public static void addCarePlan(final String id) {
        Query queryRef = CARE_PLANS_REF.orderByChild("id").equalTo(id);
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                carePlan_list.add(dataSnapshot.child(id).getValue(CarePlan.class));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("ReferralByID", firebaseError.getMessage());
            }
        });
    }

    public static void getAllCarePlans() {
        Query queryRef = CARE_PLANS_REF.orderByChild("id");
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    CarePlan carePlan = ds.getValue(CarePlan.class);
                    if (!carePlan_list.contains(carePlan)) {
                        carePlan_list.add(carePlan);
                    }

                    List<CarePlan> carePlan_pending = new ArrayList<>();
                    List<CarePlan> carePlan_Npending = new ArrayList<>();
                    for (CarePlan carePlan2 : carePlan_list) {
                        if (carePlan2.isPending()) {
                            carePlan_pending.add(carePlan2);
                        } else {
                            carePlan_Npending.add(carePlan2);
                        }
                    }

                    carePlan_list.clear();

                    Collections.sort(carePlan_pending, new Comparator<CarePlan>() {
                        @Override
                        public int compare(CarePlan lhs, CarePlan rhs) {

                            return rhs.getDateOfimport().compareTo(lhs.getDateOfimport());
                        }
                    });

                    Collections.sort(carePlan_Npending, new Comparator<CarePlan>() {
                        @Override
                        public int compare(CarePlan lhs, CarePlan rhs) {

                            return rhs.getDateOfimport().compareTo(lhs.getDateOfimport());
                        }
                    });

                    carePlan_list.addAll(carePlan_pending);
                    carePlan_list.addAll(carePlan_Npending);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("AllCarePlans", firebaseError.getMessage());
            }
        });
    }

    public static void addPatient(final String id) {
        Query queryRef = PATIENTS_REF.orderByChild("id").equalTo(id);
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                patient_list.add(dataSnapshot.child(id).getValue(edu.gatech.johndoe.carecoordinator.patient.Patient.class));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("PatientByID", firebaseError.getMessage());
            }
        });
    }

    public static void getAllPatients() {
        Query queryRef = PATIENTS_REF.orderByChild("id");
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    edu.gatech.johndoe.carecoordinator.patient.Patient p = ds.getValue(edu.gatech.johndoe.carecoordinator.patient.Patient.class);
                    if (!patient_list.contains(p)) {
                        patient_list.add(p);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("AllPatients", firebaseError.getMessage());
            }
        });
    }

    public static void addCommunity(final String id) {
        Query queryRef = COMMUNITES_REF.orderByChild("id").equalTo(id);
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                community_list.add(dataSnapshot.child(id).getValue(Community.class));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("CommunityID", firebaseError.getMessage());
            }
        });
    }

    public static void getAllCommunities() {
        Query queryRef = COMMUNITES_REF.orderByChild("id");
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LatLongUpdate communitiesLatLong = new LatLongUpdate();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    GenericTypeIndicator<Map<String, Object>> ti = new GenericTypeIndicator<Map<String, Object>>() {};
                    Map<String, Object> cr = ds.getValue(ti);
                    switch (cr.get("communityType").toString()) {
                        case "nutritionist":
                            community_list.add(ds.getValue(Nutritionist.class));
                            break;
                        case "physical":
                            community_list.add(ds.getValue(Physical.class));
                            break;
                        case "restaurant":
                            community_list.add(ds.getValue(Restaurant.class));
                            break;
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("AllCommunities", firebaseError.getMessage());
            }
        });
    }

    public static void updateCommunityLatLong(String id){
        LatLongUpdate latLongTask = new LatLongUpdate();
        List<Double> latLongResult = new ArrayList<>();
        for (Community community : community_list){
            if (community.getId().equals(id)){
                if (community.getLatitude() == 0 || community.getLongitude() == 0) {
                    try {
                        latLongResult = latLongTask.execute(community.getFullAddress()).get();
                        if (latLongResult.get(0) != -1.0) {
                            community.setLatitude(latLongResult.get(0));
                            community.setLongitude(latLongResult.get(1));
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                break;
            }
        }
    }

    public static void updateReferralStatus(String id, boolean status) {
        Map<String, Object> container = new HashMap<>();
//        container.put(id + "/pending", status);
        CARE_PLANS_REF.updateChildren(container);
        for (CarePlan carePlan : carePlan_list) {
            if (carePlan.getId().equals(id)) {
                carePlan.setPending(status);
                break;
            }
        }
    }

    public static List<CarePlan> getAllRelatedReferrals(List<String> referralIDs) {
        List<CarePlan> result = new ArrayList<>();
        if (referralIDs != null) {
            for (CarePlan carePlan : carePlan_list) {
                for (String id : referralIDs) {
                    if (id.equals(carePlan.getId())) {
                        result.add(carePlan);
                        break;
                    }
                }
            }
            Collections.sort(result, new Comparator<CarePlan>() {
                @Override
                public int compare(CarePlan e1, CarePlan e2) {
                    return Integer.valueOf(e1.getId()) - Integer.valueOf(e2.getId());
                }
            });
        }
        return result;
    }

    public static void updateCarePlans(final Context context,
                                       final ContentListFragment contentListFragment,
                                       final FragmentTransaction transaction,
                                       final boolean isInExpandedMode,
                                       final boolean refresh,
                                       final boolean toast) {

        Query queryRef = CARE_PLANS_REF.orderByChild("id");
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<CarePlan> updated = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren())
                    updated.add(ds.getValue(CarePlan.class));
                carePlan_list = updated;

                if (refresh && MainActivity.currentNavigationItemId == R.id.nav_care_plans) {
                    contentListFragment.setAdapter(
                            new CarePlanListAdapter(Utility.carePlan_list),
                            ContentListFragment.ContentType.Referral);

                    if (isInExpandedMode) {
                        //FIXME: replace with updated referral detail if needed
                        for (CarePlan e : carePlan_list) {
                            if (CarePlanListAdapter.currentCarePlan != null) {
                                if (e.getId().equals(CarePlanListAdapter.currentCarePlan.getId())) {
                                    transaction.replace(
                                            R.id.detailFragmentContainer,
                                            CarePlanDetailFragment.newInstance(e),
                                            "detail").commit();
                                    break;
                                }
                            }
                        }
                        transaction.replace(R.id.detailFragmentContainer, new UnselectedFragment(), "detail").commit();

                    }
                }

                // TODO link care plans to patients

                if (toast)
                    Toast.makeText(context, UPDATE_MESSAGE, Toast.LENGTH_LONG).show();

                Log.i("Update Referrals", "Done with updating referrals.");
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("Update Referrals", firebaseError.getMessage());
            }
        });
    }

    public static void updatePatients(final Context context,
                                      final ContentListFragment contentListFragment,
                                      final FragmentTransaction transaction,
                                      final boolean isInExpandedMode,
                                      final boolean refresh,
                                      final boolean toast) {

        Query queryRef = PATIENTS_REF.orderByChild("id");
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<edu.gatech.johndoe.carecoordinator.patient.Patient> updated = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren())
                    updated.add(ds.getValue(edu.gatech.johndoe.carecoordinator.patient.Patient.class));
                patient_list = updated;

                if (refresh && MainActivity.currentNavigationItemId == R.id.nav_patients) {
                    contentListFragment.setAdapter(
                            new PatientAdapter(Utility.patient_list),
                            ContentListFragment.ContentType.Patient);

                    if (isInExpandedMode) {
                        for (edu.gatech.johndoe.carecoordinator.patient.Patient p : patient_list) {
                            if (PatientAdapter.currentPatient != null) {
                                if (p.getId().equals(PatientAdapter.currentPatient.getId())) {
                                    transaction.replace(
                                            R.id.detailFragmentContainer,
                                            PatientDetailFragment.newInstance(p, Utility.getAllRelatedReferrals(p.getReferralList())),
                                            "detail").commit();
                                    break;
                                }
                            }
                        }
                    }
                }

                if (toast)
                    Toast.makeText(context, UPDATE_MESSAGE, Toast.LENGTH_LONG).show();

                Log.i("Update Patients", "Done with updating patients.");
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("Update Patients", firebaseError.getMessage());
            }
        });
    }

    public static void updateCommunityResources(final Context context,
                                                final ContentListFragment contentListFragment,
                                                final FragmentTransaction transaction,
                                                final boolean isInExpandedMode,
                                                final boolean refresh,
                                                final boolean toast) {

        Query queryRef = COMMUNITES_REF.orderByChild("id");
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Community> updated = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    GenericTypeIndicator<Map<String, Object>> ti = new GenericTypeIndicator<Map<String, Object>>() {};
                    Map<String, Object> cr = ds.getValue(ti);
                    switch (cr.get("communityType").toString()) {
                        case "nutritionist":
                            updated.add(ds.getValue(Nutritionist.class));
                            break;
                        case "physical":
                            updated.add(ds.getValue(Physical.class));
                            break;
                        case "restaurant":
                            updated.add(ds.getValue(Restaurant.class));
                            break;
                    }
                }
                community_list = updated;

                if (refresh && MainActivity.currentNavigationItemId == R.id.nav_communities) {
                    contentListFragment.setAdapter(
                            new CommunityAdapter(Utility.community_list),
                            ContentListFragment.ContentType.Community);

                    if (isInExpandedMode) {
                        for (Community c : community_list) {
                            if (CommunityAdapter.currentCommunity != null) {
                                if (c.getId().equals(CommunityAdapter.currentCommunity.getId())) {
                                    transaction.replace(
                                            R.id.detailFragmentContainer,
                                            CommunityDetailFragment.newInstance(c),
                                            "detail").commit();
                                    break;
                                }
                            }
                        }
                    }
                }

                if (toast)
                    Toast.makeText(context, UPDATE_MESSAGE, Toast.LENGTH_LONG).show();

                Log.i("Update Patients", "Done with updating patients.");
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("Update Patients", firebaseError.getMessage());
            }
        });

        if (toast)
            Toast.makeText(context, UPDATE_MESSAGE, Toast.LENGTH_LONG).show();

        Log.i("Update Communities", "Done with updating communities.");
    }

    public static void update(final Context context,
                              final ContentListFragment contentListFragment,
                              final FragmentTransaction transaction,
                              final boolean isInExpandedMode,
                              final int id) {
        String targets = "012";
        targets.replace(String.valueOf(id), "");
        switch (id) {
            case 0:
                updateCarePlans(context, contentListFragment,
                        transaction, isInExpandedMode, true, true);
                break;
            case 1:
                updatePatients(context, contentListFragment,
                        transaction, isInExpandedMode, true, true);
                break;
            case 2:
                updateCommunityResources(context, contentListFragment,
                        transaction, isInExpandedMode, true, true);
                break;
        }
        for (int i = 0; i < targets.length(); i++) {
            switch (Integer.valueOf(targets.charAt(i))) {
                case 0:
                    updateCarePlans(context, contentListFragment,
                            transaction, isInExpandedMode, false, false);
                    break;
                case 1:
                    updatePatients(context, contentListFragment,
                            transaction, isInExpandedMode, false, false);
                    break;
                case 2:
                    updateCommunityResources(context, contentListFragment,
                            transaction, isInExpandedMode, false, false);
                    break;
            }
        }
    }

    public static void fhirUpdate() {
        //TODO

    }

    public static ca.uhn.fhir.model.dstu2.resource.Patient getFHIRPatientByID(int id) {

        // Obtain the results from the patient query to the FHIR server
        Bundle results = client.search()
                .forResource(Patient.class)
                .where(Patient.IDENTIFIER.exactly()
                        .systemAndIdentifier("uniqueId", "99999" + String.valueOf(id)))
                .returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
                .execute();
        ca.uhn.fhir.model.dstu2.resource.Patient patient = null;
        if (results.getEntry().size() == 0) {
            System.out.println("No results matching the search criteria!");
        } else {
            patient = (Patient) results.getEntry().get(0).getResource();
        }
        return patient;
    }

    public static ca.uhn.fhir.model.dstu2.resource.CarePlan getFHIRCarePlanByID(int id) {
        // Obtain the results from the query to the FHIR server
        Bundle results = client.search()
                .forResource(ca.uhn.fhir.model.dstu2.resource.CarePlan.class)
                .where(ca.uhn.fhir.model.dstu2.resource.CarePlan.RES_ID.matchesExactly().value(String.valueOf(id)))
                .returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
                .execute();
        ca.uhn.fhir.model.dstu2.resource.CarePlan carePlan = null;
        if (results.getEntry().size() == 0) {
            System.out.println("No results matching the search criteria!");
        } else {
            carePlan = (ca.uhn.fhir.model.dstu2.resource.CarePlan) results.getEntry().get(0).getResource();
        }
        return carePlan;
    }

    public static ArrayList<Community> getCommunities() {
        ArrayList<Community> communities = new ArrayList<>(Arrays.asList(
                new Community("YMCA11", 120), new Community("Farmer's Market", 54),
                new Community("YMCA", 120), new Community("Farmer's Market", 54),
                new Community("YMCA", 120), new Community("Farmer's Market", 54),
                new Community("YMCA", 120), new Community("Farmer's Market", 54),
                new Community("YMCA", 120), new Community("Farmer's Market", 54),
                new Community("YMCA", 120), new Community("Farmer's Market", 54),
                new Community("YMCA", 120), new Community("Farmer's Market", 54),
                new Community("YMCA", 120), new Community("Farmer's Market", 54),
                new Community("YMCA", 120), new Community("Farmer's Market", 54),
                new Community("YMCA", 120), new Community("Farmer's Market", 54),
                new Community("YMCA", 120), new Community("Farmer's Market", 54),
                new Community("YMCA", 120), new Community("Farmer's Market", 54),
                new Community("YMCA", 120), new Community("Farmer's Market", 54),
                new Community("YMCA", 120), new Community("Farmer's Market", 54),
                new Community("YMCA", 120), new Community("Farmer's Market", 54)
        ));  // FIXME: replace with real data

        return communities;
    }


}
