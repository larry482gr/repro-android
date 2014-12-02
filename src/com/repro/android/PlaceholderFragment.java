package com.repro.android;

import java.util.Locale;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.repro.android.adapters.MembersAdapter;
import com.repro.android.adapters.NewsAdapter;
import com.repro.android.asynctasks.SendEmail;
import com.repro.android.dialogs.Dialogs;
import com.repro.android.entities.ArticlesModel;
import com.repro.android.entities.MembersModel;
import com.repro.android.entities.StaticContent;
import com.repro.android.utilities.HtmlTagHandler;
import com.repro.android.utilities.NetworkUtilities;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {
	private String TAG = "PlaceholderFragment";
	
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";
	
	private static int tabId;

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static PlaceholderFragment newInstance(int sectionNumber) {
		PlaceholderFragment fragment = new PlaceholderFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public PlaceholderFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		tabId = getArguments().getInt(ARG_SECTION_NUMBER);
		int fragment = getFragment(tabId);
		View rootView = inflater.inflate(fragment, container, false);
		
		return rootView;
	}
	
	public static int getTabId() {
		return tabId;
	}
	
	@Override
	public void onViewCreated(View rootView, Bundle savedInstanceState) {
		super.onViewCreated(rootView, savedInstanceState);
		initView(rootView, tabId);
		setActionBarTitle();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		// initView(getView(), tabId);
		// setActionBarTitle();
	}

	private int getFragment(int tabId) {
		int fragmentId = -1;
		switch(tabId) {
			case 1:
				fragmentId = R.layout.fragment_members;
				break;
			case 2:
				fragmentId = R.layout.fragment_research_program;
				break;
			case 3:
				fragmentId = R.layout.fragment_news_list;
				break;
			case 8:
				fragmentId = R.layout.fragment_contact;
				break;
			default:
				fragmentId = R.layout.fragment_main;
				break;
		}
		
		return fragmentId;
	}
	
	private void initView(View rootView, int tabId) {
		switch(tabId) {
			case 1:
				initMembersFragment(rootView);
				break;
			case 2:
				initResearchProgramFragment(rootView);
				break;
			case 3:
				initNewsFragment(rootView);
				break;
			case 8:
				initContactFragment(rootView);
				break;
			default:
				initMainFragment(rootView);
				break;
		}
	}

	private void setActionBarTitle() {
		String[] menuItems = getResources().getStringArray(R.array.menu_items);
		getActivity().getActionBar().setTitle(menuItems[tabId-1]);
		
	}

	private void initMembersFragment(final View rootView) {
		Handler mHandler = new Handler(getActivity().getMainLooper());
		
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// Implement members GridView adapter.
				GridView membersGrid = (GridView) rootView.findViewById(R.id.members_grid);
				MembersModel membersModel = new MembersModel(getActivity());
				Cursor groups = membersModel.findGroups();
				Cursor membersCursor = membersModel.findMembers(groups);
				if(null != membersCursor) {
					MembersAdapter mAdapter = new MembersAdapter(getActivity(), membersCursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
					membersGrid.setAdapter(mAdapter);
				}
			}
		}, 340);
	}
	
	private void initResearchProgramFragment(final View rootView) {
		Handler mHandler = new Handler(getActivity().getMainLooper());
		
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				Configuration config = rootView.getContext().getResources().getConfiguration();
		        Locale locale = config.locale;
		        
				TextView researchProgram = (TextView) rootView.findViewById(R.id.research_program);
				HtmlTagHandler htmlTagHandler = new HtmlTagHandler();
				
				if(locale.toString().equals("el_GR")) {
					researchProgram.setText(Html.fromHtml(StaticContent.RESEARCH_PROGRAM_TITLE_GR + StaticContent.RESEARCH_PROGRAM_CONTENT_GR, null, htmlTagHandler));
				}
				else {
					researchProgram.setText(Html.fromHtml(StaticContent.RESEARCH_PROGRAM_TITLE_EN + StaticContent.RESEARCH_PROGRAM_CONTENT_EN, null, htmlTagHandler));
				}
			}
		}, 340);
	}
	
	private void initNewsFragment(final View rootView) {
		Handler mHandler = new Handler(getActivity().getMainLooper());
		
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				ListView newsList = (ListView) rootView.findViewById(R.id.news_list);
				ArticlesModel articlesModel = new ArticlesModel(getActivity());
				Cursor articlesCursor = articlesModel.findArticles();
				NewsAdapter mAdapter = new NewsAdapter(getActivity(), articlesCursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
				newsList.setAdapter(mAdapter);
			}
		}, 340);
	}
	
	private void initContactFragment(View rootView) {
		Resources resources = getActivity().getResources();
        
        final TextView contactFormTitle = (TextView) rootView.findViewById(R.id.contact_form_title);
        final TextView fullNameLabel = (TextView) rootView.findViewById(R.id.full_name_label);
        final TextView subjectLabel = (TextView) rootView.findViewById(R.id.subject_label);
        final TextView messageLabel = (TextView) rootView.findViewById(R.id.message_label);
		final EditText fullNameInput = (EditText) rootView.findViewById(R.id.full_name_input);
		final EditText emailInput = (EditText) rootView.findViewById(R.id.email_input);
		final EditText subjectInput = (EditText) rootView.findViewById(R.id.subject_input);
		final EditText messageInput = (EditText) rootView.findViewById(R.id.message_input);
		final TextView allRequired = (TextView) rootView.findViewById(R.id.all_required);
		final Button sendButton = (Button) rootView.findViewById(R.id.send_button);
		
		contactFormTitle.setText(resources.getString(R.string.contact_form));
		fullNameLabel.setText(resources.getString(R.string.contact_full_name));
		subjectLabel.setText(resources.getString(R.string.contact_subject));
		messageLabel.setText(resources.getString(R.string.contact_message));
		sendButton.setText(resources.getString(R.string.send_button));
		
		sendButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final Handler mHandler = new Handler(Looper.getMainLooper());
				
				if(NetworkUtilities.isConnectedToInternet(getActivity())) {
					String[] contactParams = new String[] { fullNameInput.getText().toString().trim(),
															emailInput.getText().toString().trim(),
															subjectInput.getText().toString().trim(),
															messageInput.getText().toString().trim(),
															"0" };
					
					boolean noEmptyField = true;
					for(String param : contactParams) {
						if(param.length() == 0) {
							noEmptyField = false;
							showErrorMessage(mHandler, allRequired);
							break;
						}
					}
					
					if(noEmptyField) {
						SendEmail sendEmail = new SendEmail(getActivity());
						sendEmail.execute(contactParams);
					}
				} else {
					String title = getResources().getString(R.string.email_no_connection_msg);
					String[] options = new String[] { getResources().getString(R.string.enable_wifi), getResources().getString(R.string.enable_mobile) };
					android.content.DialogInterface.OnClickListener connect = new android.content.DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							final ProgressDialog enablingConnection = new ProgressDialog(getActivity());
							if(which == 0) {
								NetworkUtilities.enableWifi(getActivity());
								enablingConnection.setMessage(getActivity().getResources().getString(R.string.enabling_wifi));
							}
							else if(which == 1) {
								NetworkUtilities.enableMobileData(getActivity());
								enablingConnection.setMessage(getActivity().getResources().getString(R.string.enabling_mobile));
							}
							enablingConnection.show();
							
							for(int i = 1; i < 6; i++) {
								mHandler.postDelayed(new Runnable() {
									public void run() {
										if(NetworkUtilities.isConnectedToInternet(getActivity())) {
											enablingConnection.dismiss();
										}
								    }
								}, i*2000);
							}
							
							mHandler.postDelayed(new Runnable() {
								public void run() {
									if(!NetworkUtilities.isConnectedToInternet(getActivity())) {
										enablingConnection.dismiss();
										String toastMessage = getActivity().getResources().getString(R.string.connection_failure) + "\n" + 
															  getActivity().getResources().getString(R.string.check_network);
										Toast.makeText(getActivity(), toastMessage, Toast.LENGTH_LONG).show();
									}
							    }
							}, 11000);
							
						}
					};
					Builder alertDialog = Dialogs.optionDialog(getActivity(), title, options, connect);
					alertDialog.create().show();
				}
			}
		});
	}
	
	private void showErrorMessage(final Handler mHandler, final TextView allRequired) {
		allRequired.setAlpha(0f);
		allRequired.setScaleX(0f);
		allRequired.setScaleY(0f);
		allRequired.setVisibility(View.VISIBLE);
		allRequired.animate()
				   .scaleX(1f)
				   .scaleY(1f)
				   .alpha(1f)
				   .setDuration(500)
				   .setListener(new AnimatorListenerAdapter() {
		                @Override
		                public void onAnimationEnd(Animator animation) {
		                	mHandler.postDelayed(new Runnable() {
								public void run() {
									allRequired.animate()
									   .alpha(0f)
									   .setDuration(200)
									   .setListener(new AnimatorListenerAdapter() {
							                @Override
							                public void onAnimationEnd(Animator animation) {
							                	allRequired.setVisibility(View.INVISIBLE);
							                }
							            });
							    }
							}, 3000);
		                }
				   });
	}

	private void initMainFragment(View rootView) {
		TextView fragment_text = (TextView) rootView.findViewById(R.id.section_label);
		String[] menuItems = getResources().getStringArray(R.array.menu_items);
		fragment_text.setText(menuItems[tabId-1]);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
	}
}