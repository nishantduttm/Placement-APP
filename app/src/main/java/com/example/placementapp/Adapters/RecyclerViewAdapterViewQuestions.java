//package com.example.placementapp.Adapters;
//
//import android.content.Context;
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.placementapp.R;
//import com.example.placementapp.pojo.QuestionDto;
//
//import java.util.List;
//
//public class RecyclerViewAdapterViewQuestions extends RecyclerView.Adapter<RecyclerViewAdapterViewQuestions.MyViewHolder> {
//
//    List<QuestionDto> questionList;
//    Context context;
//
//    public RecyclerViewAdapterViewQuestions(Context context, List<QuestionDto> questionList)
//    {
//        this.context = context;
//        this.questionList = questionList;
//    }
//
//    @Override
//    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//// infalte the item Layout
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_row_design, parent, false);
//// set the view's size, margins, paddings and layout parameters
//        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
//        return vh;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        QuestionDto question = questionList.get(position);
//        holder.questionTF.setText(question.getQuestion());
//
//        holder.writeAnswer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(view.getContext(),AnswerSubmitActivity.class);
//                intent.putExtra("question",questionList.get(position).getQuestion());
//                intent.putExtra("questionID",questionList.get(position).getQuestionID());
//                view.getContext().startActivity(intent);
//            }
//        });
//
//
//        holder.readAnswer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(view.getContext(), ViewAnswersActivity.class);
//                intent.putExtra("question",questionList.get(position).getQuestion());
//                intent.putExtra("questionID",questionList.get(position).getQuestionID());
//                view.getContext().startActivity(intent);
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return questionList.size();
//    }
//
//    public class MyViewHolder extends RecyclerView.ViewHolder {
//        // init the item view's
//        TextView questionTF;
//        ImageView writeAnswer;
//        ImageView readAnswer;
//        public MyViewHolder(final View itemView) {
//            super(itemView);
//
//// get the reference of item view's
//            questionTF = (TextView) itemView.findViewById(R.id.questionTV);
//            writeAnswer = itemView.findViewById(R.id.write_answer);
//            readAnswer = itemView.findViewById(R.id.read_answer);
//        }
//    }
//}
