package com.app.bombill.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.app.bombill.R;
import com.bumptech.glide.Glide;

public class Fish_Nutriation extends AppCompatActivity {

    ImageView bombill_images,Bombill_imageNutrionfo,bangada_Image,Bangda_image_info,
            vaam_image,Vaam_image_info,jitada_image,Jitada_image_info,sumai_image,Sumai_image_info,prawns_image,Prawns_image_info;

    String a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fish_nutriation2);
        bombill_images=findViewById(R.id.bombill_image);
        Bombill_imageNutrionfo=findViewById(R.id.bombill_image_nutriinfo);
        bangada_Image=findViewById(R.id.bangda_image);
        Bangda_image_info=findViewById(R.id.bangda_image_info);
        vaam_image=findViewById(R.id.vaam_image);
        Vaam_image_info=findViewById(R.id.vaam_image_info);
        jitada_image=findViewById(R.id.jitada_image);
        Jitada_image_info=findViewById(R.id.jitada_image_info);
        sumai_image=findViewById(R.id.surmai_image);
        Sumai_image_info=findViewById(R.id.surmai_image_info);
        prawns_image=findViewById(R.id.prawm_image);
        Prawns_image_info=findViewById(R.id.prawn_image_info);

        //for bombill
        Glide.with(this)
                .load("https://new.bombill.com/images/bombill_1.png")
                .into(bombill_images);
        Glide.with(this)
                .load("https://new.bombill.com/images/bombill.png")
                .into(Bombill_imageNutrionfo);

        //for bangda
        Glide.with(this)
                .load("https://new.bombill.com/images/bangda%20(1).png")
                .into(bangada_Image);
        Glide.with(this)
                .load("https://new.bombill.com/images/Bangda.png")
                .into(Bangda_image_info);

        //for Vaam
        Glide.with(this)
                .load("https://new.bombill.com/images/vaam%20(1).png")
                .into(vaam_image);
        Glide.with(this)
                .load("https://new.bombill.com/images/Vaam.png")
                .into(Vaam_image_info);


        //for Jitada
        Glide.with(this)
                .load("https://new.bombill.com/images/jitada%20(1).png")
                .into(jitada_image);
        Glide.with(this)
                .load("https://new.bombill.com/images/jitada.png")
                .into(Jitada_image_info);

        //for Surmai
        Glide.with(this)
                .load("https://new.bombill.com/images/surmai%20(1).png")
                .into(sumai_image);
        Glide.with(this)
                .load("https://new.bombill.com/images/surmai.png")
                .into(Sumai_image_info);

        //for Prawans
        Glide.with(this)
                .load("https://new.bombill.com/images/pomfret_1.png")
                .into(prawns_image);
        Glide.with(this)
                .load("https://new.bombill.com/images/prawns.png")
                .into(Prawns_image_info);
    }
}