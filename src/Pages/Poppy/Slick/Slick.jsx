import React from 'react';
import Slider from 'react-slick';
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";
import Item from '../Item/Item';
import sun from '../../../Images/listsun1.png';
import pre from '../../../Images/Group 184.png';
import next from '../../../Images/Group 185 (1).png';
import './Slick.css'

const CustomPrevArrow = (props) => (
    <div
        className="custom-prev-arrow"
        onClick={props.onClick}
    >
        <img src={pre} alt="" />
    </div>
);

const CustomNextArrow = (props) => (
    <div
        className="custom-next-arrow"
        onClick={props.onClick}
    >
        <img src={next} alt="" />
    </div>
);

const ListArray =[
    {
        time : "6:00",
        image : sun,
        doam : "79",
        desscription : "Nhiều mây",
        nhietdo1 :  "21",
        nhietdo2 :  "22",
    },
    {
        time : "7:00",
        image : sun,
        doam : "77",
        desscription : "Nhiều mây",
        nhietdo1 :  "21",
        nhietdo2 :  "22",
    },
    {
        time : "8:00",
        image : sun,
        doam : "74",
        desscription : "Nhiều mây",
        nhietdo1 :  "21",
        nhietdo2 :  "22",
    },
    {
        time : "9:00",
        image : sun,
        doam : "17",
        desscription : "Nhiều mây",
        nhietdo1 :  "21",
        nhietdo2 :  "22",
    },
    {
        time : "18:00",
        image : sun,
        doam : "79",
        desscription : "Nhiều mây",
        nhietdo1 :  "21",
        nhietdo2 :  "22",
    }
]

const Slick = () => {
    var settings = {
        dots: true,
        infinite: true,
        autoplay: true,
        autoplaySpeed: 2000,
        slidesToShow: 4,
        slidesToScroll: 1,
        ltr: true,
        cssEase: 'linear',
        prevArrow: <CustomPrevArrow />,
        nextArrow: <CustomNextArrow />
    };
    return (
        <div className='p-2' style={{ height: "50px",background:"#3178E1" }}>
            <Slider {...settings} className='container-fluid'>
                {
                    ListArray.map((e, index) => (
                        <Item key={index} ten={e.time} phantram={e.doam} img={e.image} des={e.desscription} nhietdo1={e.nhietdo1} nhietdo2={e.nhietdo2}/>
                    ))
                }
            </Slider>
        </div>
    );
}

export default Slick;
