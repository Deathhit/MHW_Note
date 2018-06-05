package tw.com.deathhit.mhw_note.utility.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import tw.com.deathhit.mhw_note.core.BaseFragment;

/**BaseFragment class that is specialized to deal with text input. Invoke registerEditorView() to
 * register your editors.**/
public abstract class EditorFragment extends BaseFragment implements TextView.OnEditorActionListener{
    private SparseArray<WeakReference<TextView>> editorViews = new SparseArray<>();

    private SparseBooleanArray validationResults = new SparseBooleanArray();

    private boolean actOnTextChanged = false;
    private boolean hideSoftKeyboardOnDone = true;

    @Override
    public void onCreate(Bundle savedInstanceStates){
        super.onCreate(savedInstanceStates);

        validationResults.clear();  //validationResults needs to be clear when view is recreated
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        //DONE is pressed
        if(actionId == EditorInfo.IME_ACTION_DONE){
            if(!isInputValid())
                return false;

            if(hideSoftKeyboardOnDone) {
                InputMethodManager inputManager = (InputMethodManager)
                        getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.toggleSoftInput(0, 0);
            }

            onInputValid(); //Final action after input complete

            return true;
        }

        return false;
    }

    private void validateAndAct(TextView editorView){
        CharSequence input = editorView.getText();

        boolean result = validateInput(editorView, input);

        onValidated(editorView, input, result);

        //Check if validation result is changed
        if(result != validationResults.get(editorView.getId(), !result)) {
            validationResults.put(editorView.getId(), result);

            onResultChanged(editorView, input, result);
        }
    }

    /**Return the corresponding editor view.**/
    protected TextView getEditor(int editorId){
        return editorViews.get(editorId).get();
    }

    /**Check if every input is valid. This function is used to decide if onInputComplete()
     * needs to be called, and it triggers both validateEditorInput() and onResultChanged.**/
    protected boolean isInputValid(){
        boolean result = true;

        for(int i=0; i<editorViews.size();i++){
            int key = editorViews.keyAt(i);

            TextView view = editorViews.get(key).get();

            if(view != null)
                validateAndAct(view);
            else
                continue;

            if(!validationResults.get(key))
                result = false;
        }

        return result;
    }

    /**Override to implement actions after validateEditorInput(). This method is called every time
     * validateEditorInput() takes place.**/
    protected void onValidated(TextView editorView, CharSequence input, boolean validationResult){

    }

    /**Register editor view to the fragment.**/
    protected void registerEditorView(final TextView editorView){
        editorView.setOnEditorActionListener(this);

        editorView.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(actOnTextChanged)
                    validateAndAct(editorView);
            }
        });

        editorViews.put(editorView.getId(), new WeakReference<>(editorView));
    }

    /**Unregister editor view.**/
    protected void unregisterEditorView(TextView editorView){
        for(int i=0;i<editorViews.size();i++){
            TextView textView = editorViews.get(editorViews.keyAt(i)).get();

            if(textView == editorView){
                editorViews.removeAt(i);
                editorView.setOnEditorActionListener(null);

                break;
            }
        }
    }

    /**If set false, onValidated() and onValidationChanged() will only be invoked by isInputValid().
     * Modifying editor input will not trigger validateEditorInput(). If set true, things go to otherwise.**/
    public void setActOnTextChanged(boolean actOnTextChanged){
        this.actOnTextChanged = actOnTextChanged;
    }

    /**If hideSoftKeyboardOnDone is true, hide soft keyboard when EditorInfo.IME_ACTION_DONE is detected.**/
    public void setHideSoftKeyboardOnDone(boolean hideSoftKeyboardOnDone){
        this.hideSoftKeyboardOnDone = hideSoftKeyboardOnDone;
    }

    /**Implement input validation in this function. Validation on input occurs immediately after you
     * invoked registerEditorView()**/
    public abstract boolean validateInput(TextView editorView, CharSequence input);

    /**Implement actions after the result of validateEditorInput() is changed.**/
    public abstract void onResultChanged(TextView editorView, CharSequence input, boolean validationResult);

    /**Implement action if every input is valid.**/
    public abstract void onInputValid();
}