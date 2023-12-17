import React, { useEffect, useState } from 'react';
import './HomePage.css'
import MainLayout from '../../components/Layouts/MainLayout/MainLayout';
import sun from '../../Images/Sun Cloud.png'
import nhietdo from '../../Images/Ellipse 52.png'
import doc from '../../Images/Ellipse 53.png'
import c from '../../Images/C.png'
import f from '../../Images/F.png'
import s from '../../Images/sun.png'
import giam from '../../Images/Frame 12.png'
import tang from '../../Images/Frame 13.png'
import cao from '../../Images/thapcao.png';
import doam from '../../Images/doam.png';
import apsuat1 from '../../Images/Line 30.png';
import apsuat2 from '../../Images/Line 31.png';
import uv from '../../Images/Group 159.png';
import diemngung from '../../Images/Humidity.png';
import gio from '../../Images/Blowing-snow.png';
import tamnhin from '../../Images/Ellipse 70.png';
import Item from '../Poppy/Item/Item';
import link from '../../Images/Group 17.png';
import link2 from '../../Images/Group 186.png';
import Weather from '../Poppy/Weather/Weather';
import image1 from '../../Images/Rectangle 926.png'
import image2 from '../../Images/Rectangle 928.png'
import axios from 'axios';
import toast from 'react-hot-toast';

///////////////////////////////dữ liệu tạm thời , có api thì xóa 

const ListWeatherCity =[
    {
        ten : "Lào Cai",
        phantram : "57",
        des :"Mây rải rác",
        img :sun,
        nhietdo1 : "31",
        nhietdo2 : "32"
    },
    {
        ten : "Lào Cai",
        phantram : "57",
        des :"Mây rải rác",
        img :sun,
        nhietdo1 : "31",
        nhietdo2 : "32"
    },
    {
        ten : "Lào Cai",
        phantram : "57",
        des :"Mây rải rác",
        img :sun,
        nhietdo1 : "31",
        nhietdo2 : "32"
    },
    {
        ten : "Lào Cai",
        phantram : "57",
        des :"Mây rải rác",
        img :sun,
        nhietdo1 : "31",
        nhietdo2 : "32"
    },
    {
        ten : "Lào Cai",
        phantram : "57",
        des :"Mây rải rác",
        img :sun,
        nhietdo1 : "31",
        nhietdo2 : "32"
    },
    {
        ten : "Lào Cai",
        phantram : "57",
        des :"Mây rải rác",
        img :sun,
        nhietdo1 : "31",
        nhietdo2 : "32"
    },
    {
        ten : "Lào Cai",
        phantram : "57",
        des :"Mây rải rác",
        img :sun,
        nhietdo1 : "31",
        nhietdo2 : "32"
    },
    {
        ten : "Lào Cai",
        phantram : "57",
        des :"Mây rải rác",
        img :sun,
        nhietdo1 : "31",
        nhietdo2 : "32"
    },
    {
        ten : "Lào Cai",
        phantram : "57",
        des :"Mây rải rác",
        img :sun,
        nhietdo1 : "31",
        nhietdo2 : "32"
    },
    {
        ten : "Lào Cai",
        phantram : "57",
        des :"Mây rải rác",
        img :sun,
        nhietdo1 : "31",
        nhietdo2 : "32"
    },
    {
        ten : "Lào Cai",
        phantram : "57",
        des :"Mây rải rác",
        img :sun,
        nhietdo1 : "31",
        nhietdo2 : "32"
    },
    {
        ten : "Lào Cai",
        phantram : "57",
        des :"Mây rải rác",
        img :sun,
        nhietdo1 : "31",
        nhietdo2 : "32"
    },
]


// sử dụng để hiển thị dự báo thời tiết demo  của ngày hôm nay 
const dbWeatherToday = {
    address:"TP.Hồ Chí Minh",
    update : "10",
    status : "Mây rải rác",
    LikeTemperature : "Cảm giác như 38",
    temperature : "33"
}

const overview = {
    sunrise : "21",
    sunset :"21",
    hight : "19",
    short : "19",
    humidity : "70",
    Pressure : "1010mb",
    vision:"10km",
    wind : "3.6km/h",
    pointStop : "23",
    UV : "0"
}



//////////////////////////////////////////////
const HomePage = () => {

    const [data,setData] = useState("") // cập nhật lại dữ liệu cần hiển thị , có thể tạo nhiều để lấy dữ liệu khác nhau ở các bảng
    useEffect(()=>{
        getdata()
    },[])
    const getdata = async ()=>{
        try {
            const {data} = await axios.get("/api/v1/weather/getall-weather") // địa chỉ api để lấy dữ liệu
            if(data?.success) // nếu bên backend trả về susscess : true thì cập nhật lại data 
             {
                setData(data?.weather) // dữ liệu trả về sẽ được cập nhật cho data bằng phương thức setData
            }
        } catch (error) {
            console.log(error) // kết nối không được thì sẽ hiển thị lỗi
            toast.error("Some thing went wrong in weather")
        }
    }

    return (
        <MainLayout title={"Dự báo thời tiết"}>
            <div className="wrapper_home">
                <div className="weather_home container" style={{background:"#206CDF"}}>
                    <div className="row">
                        <div className="col-8">
                            <h6 className='title_weather'>DỰ BÁO THỜI TIẾT CÁC TỈNH THÀNH PHỐ</h6>
                            <div className="package m-0">
                                <div className="row package_home justify-content-between mt-3">
                                    {
                                        ListWeatherCity.map((e,index)=>(
                                            <Item key={index} ten={e.ten} phantram={e.phantram} des={e.des}  img={sun} nhietdo1={e.nhietdo1} nhietdo2={e.nhietdo2}/>
                                        ))
                                    }
                                </div>
                            </div>
                        </div>
                        <div className="col-4 p-3 mt-4">
                            <div className="dubao_tinh">
                                <div className="weather_city">
                                    <div>
                                        <h6>{dbWeatherToday.address}</h6>
                                        <p>Đã cập nhật {dbWeatherToday.update} phút trước</p>
                                    </div>
                                    <div className="d-flex align-center justify-content-center">
                                        <img src={sun} alt=""/>
                                        <h1 style={{fontSize:"3.5rem"}}>{dbWeatherToday.temperature}</h1>
                                        <img style={{marginRight:"6px"}} width={"25px"} height={"25px"} src={nhietdo} alt="" />
                                        <div>
                                            <img style={{display:"block",marginBottom:"6px"}} width={"20px"} height={"25px"} src={c} alt="" />
                                            <img style={{display:"block"}} width={"20px"} height={"25px"} src={f} alt="" />
                                        </div>
                                    </div>
                                    <div className="">
                                        <strong>{dbWeatherToday.status}</strong>
                                        <p style={{position:"relative"}}>
                                            {dbWeatherToday.LikeTemperature}
                                            <img style={{position:"absolute",top:5}} src={doc} alt="" />
                                        </p>
                                    </div>
                                </div>
                                <div className="dubao">
                                    <div className="dubao_title">
                                        <p>Dự báo những ngày tới</p>
                                        <div className="row justify-content-around row_dubao">
                                            <div className="col-3 btn btn_bao">Hôm nay</div>
                                            <div className="col-3 btn btn_bao">Ngày mai</div>
                                            <div className="col-3 btn btn_bao">3 ngày</div>
                                            <div className="col-3 btn btn_bao">5 ngày</div>
                                        </div>
                                        <div className="row justify-content-around row_dubao">
                                            <div className="col-3 btn btn_bao">7 ngày</div>
                                            <div className="col-3 btn btn_bao">10 ngày</div>
                                            <div className="col-3 btn btn_bao">15 ngày</div>
                                            <div className="col-3 btn btn_bao">30 ngày</div>
                                        </div>
                                        
                                    </div>
                                </div>

                                <div className="dubaotong mt-2">
                                    <div className="d-flex justify-content-between p-0">
                                        <div className="title_dubaotong">
                                            <img src={s} alt="" className='icon' />
                                            <span className='title_dubaotong_span' >Mặt trời mọc/lặn</span>
                                        </div>
                                        <div className="thongso_dubaotong d-flex" >
                                            <img style={{paddingBottom:"10px"}} width="32px" height="32px" src={tang} alt="" />
                                            <span>{overview.sunrise}</span>
                                            <img style={{paddingBottom:"10px",marginLeft:"4px"}} width="32px" height="32px" src={giam} alt="" />
                                            <span>{overview.sunset}</span>
                                        </div>
                                    </div>
                                    <div className="d-flex justify-content-between p-0 mt-1">
                                        <div className="title_dubaotong">
                                            <img src={cao} alt=""  className='icon'/>
                                            <span className='title_dubaotong_span'>Thấp/cao</span>
                                        </div>
                                        <div className="thongso_dubaotong d-flex" >
                                            <div className="" style={{position:"relative"}}>
                                                <span>{overview.short}</span>
                                                <img style={{position:"absolute",top:5}} src={doc} alt="" />
                                            </div>
                                            <span style={{margin:"0 4px 0 10px"}}>/</span>
                                            <div className="" style={{position:"relative"}}>
                                                <span>{overview.hight}</span>
                                                <img style={{position:"absolute",top:5}} src={doc} alt="" />
                                            </div>
                                        </div>
                                    </div>
                                    <div className="d-flex justify-content-between p-0  mt-1">
                                        <div className="title_dubaotong">
                                            <img src={doam} alt="" className='icon' />
                                            <span className='title_dubaotong_span'>Độ ẩm</span>
                                        </div>
                                        <div className="thongso_dubaotong d-flex" >
                                            <span>{overview.humidity}</span>
                                        </div>
                                    </div>
                                    <div className="d-flex justify-content-between p-0  mt-1">
                                        <div className="title_dubaotong d-flex">
                                            <div style={{display:"block",marginRight:"4px"}}>
                                                <img src= {apsuat1} alt=""/>
                                                <img src= {apsuat2} alt=""/>
                                            </div>
                                            <span className='title_dubaotong_span'>Áp suất</span>
                                        </div>
                                        <div className="thongso_dubaotong d-flex" >
                                            <span>{overview.Pressure}</span>
                                        </div>
                                    </div>
                                    <div className="d-flex justify-content-between p-0  mt-1">
                                        <div className="title_dubaotong">
                                            <img src={tamnhin} alt=""  className='icon'/>
                                            <span className='title_dubaotong_span'>Tầm nhìn</span>
                                        </div>
                                        <div className="thongso_dubaotong d-flex" >
                                            <span>{overview.vision}</span>
                                        </div>
                                    </div>
                                    <div className="d-flex justify-content-between p-0  mt-1">
                                        <div className="title_dubaotong">
                                            <img src={gio} alt=""  className='icon'/>
                                            <span className='title_dubaotong_span'>Gió</span>
                                        </div>
                                        <div className="thongso_dubaotong d-flex" >
                                            <span>{overview.wind}</span>
                                        </div>
                                    </div>
                                    <div className="d-flex justify-content-between p-0 mt-1">
                                        <div className="title_dubaotong">
                                            <img src={diemngung} alt=""  className='icon'/>
                                            <span className='title_dubaotong_span'>Điểm ngưng</span>
                                        </div>
                                        <div className="thongso_dubaotong d-flex" style={{position:"relative"}} >
                                            <span>{overview.pointStop}</span>
                                            <img style={{position:"absolute",left:"55%"}} src={doc} alt="" />
                                            <span style={{paddingLeft:"8px"}}>c</span>
                                        </div>
                                    </div>
                                    <div className="d-flex justify-content-between align-items-center p-0 mt-1">
                                        <div className="title_dubaotong">
                                            <img src={uv} alt=""  className='icon'/>
                                            <span className='title_dubaotong_span'>Chỉ số UV</span>
                                        </div>
                                        <div className="thongso_dubaotong d-flex" >
                                            <span>{overview.UV}</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <h6>TIN TỨC THỜI TIẾT</h6>
                </div>
            </div>
            <div className="new_home">
                <div className='container wrapper_new'>
            <div className="row mt-5">
                <div className="col-6">
                    <img src={image1} alt="" />
                    <div className="conten_new">
                        <div className="title_new">
                            <p>Tin tổng hợp</p>
                        </div>
                        <div className="des_new">
                            <p>Tia UV là gì ? Cách bảo vệ da dưới tác hại của tia UV  </p>
                            <p>Tia UV có lẽ là kẻ thù lớn nhất đối với làn da của chúng ta. Không những thể còn chứa nhiều tiềm ẩn lớn gây tổn thương mắt và nhiều vấn đề khác. Vậy các bạn đã hiểu tia Uv là gì? Tác hại của tia Uv như thế nào đối với chúng ta hay chưa. </p>
                        </div>
                    </div>
                </div>
                <div className="col-6">
                    <div className="row mb-4">
                        <div className="col-5">
                            <img src={image2} alt="" />
                        </div>
                        <div className="col-7">
                            <p style={{marginBottom:"25px"}}>Hoàng hôn nghĩa là gì ? 6+ý nghĩa kì diệu của hoàng hôn  </p>
                            <div className="title_new">
                            <p>Tin tổng hợp</p>
                        </div>
                        </div>
                    </div>
                    <div className="row mb-4">
                        <div className="col-5">
                            <img src={image2} alt="" />
                        </div>
                        <div className="col-7">
                            <p style={{marginBottom:"25px"}}>Hoàng hôn nghĩa là gì ? 6+ý nghĩa kì diệu của hoàng hôn  </p>
                            <div className="title_new">
                            <p>Tin tổng hợp</p>
                        </div>
                        </div>
                    </div>
                    <div className="row mb-4">
                        <div className="col-5">
                            <img src={image2} alt="" />
                        </div>
                        <div className="col-7">
                            <p style={{marginBottom:"25px"}}>Hoàng hôn nghĩa là gì ? 6+ý nghĩa kì diệu của hoàng hôn  </p>
                            <div className="title_new">
                            <p>Tin tổng hợp</p>
                        </div>
                        </div>
                    </div>
                    <div className="row mb-4">
                        <div className="col-5">
                            <img src={image2} alt="" />
                        </div>
                        <div className="col-7">
                            <p style={{marginBottom:"25px"}}>Hoàng hôn nghĩa là gì ? 6+ý nghĩa kì diệu của hoàng hôn  </p>
                            <div className="title_new">
                            <p>Tin tổng hợp</p>
                        </div>
                        </div>
                    </div>
                    
                </div>
            </div>
                </div>
                <div className="container content_chinh ">
                    <div className="package_content">
                        <p>Nội dung chính</p>
                        <p>1. Cung cấp bản tin thời tiết hằng ngày  </p>
                        <p>2. Dự báo thời tiết cập nhật theo giờ  </p>
                    </div>
                    <div className="des_content">
                        <p>
                        Bạn đang tìm kiếm một công cụ tra cứu thông tin thời tiết hằng ngày tại nơi 
                        bạn đang sinh sống? Để xem tin tức cập nhật về thời tiết hôm nay, ngày mai, 
                        cũng như khả năng có mưa, nắng nóng, bão, lũ, lốc xoáy... thì đã có trang web thoitiet.vn.
                        </p>
                        <a href="#">Mở rộng</a>
                    </div>
                </div>

                <div className="title_info container d-flex">
                    <p style={{marginRight:"24px"}}>Kênh thông tin dự báo thời tiết</p>
                    <div className="">
                        <img src={link} alt="" />
                    </div>
                </div>
                <Weather/>
                <div className="mt-2" style={{background:"#D9D9D9"}}>
                    <div className="container d-flex align-items-center pt-2 pb-2" style={{background:"#D9D9D9"}}>
                        <span style={{marginRight:"50px"}}>Kênh dự báo thời tiết </span> <img src={link2} alt="" />
                    </div>
                </div>
            </div>
        </MainLayout>
    );
}

export default HomePage;
