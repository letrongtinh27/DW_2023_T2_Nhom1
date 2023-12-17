import React from 'react';
import './Item.css'
import d from '../../../Images/Ellipse 58.png';
import doam from '../../../Images/doam.png';
import sun from '../../../Images/Sun Cloud.png'

const Item = ({ten,img,phantram,des,nhietdo1,nhietdo2}) => {
    return (
        <div className="col-3 column">
            <p className="title_tinh">{ten}</p>
            <div className="tinhtrang d-flex justify-content-between">
                <img width={"68px"} src={img} alt="" />
                <div className="phantramdoam d-flex align-items-center">
                    <img src={doam} width={"15px"} style={{marginBottom:"4px"}} alt="" />
                    <span>{phantram}%</span>
                </div>
            </div>
            <p style={{fontSize:"12px"}}>{des}</p>
            <div className="d-flex justify-content-center align-items-center mt-3" style={{fontSize:"12px"}}>
                <div style={{ position: "relative" }}>
                    <span>{nhietdo1}</span>
                    <img style={{ position: "absolute", top: 5,left:"15px" }} src={d} alt="" />
                </div>
                <span style={{ margin: "0 4px 0 10px" }}>/</span>
                <div style={{ position: "relative" }}>
                    <span>{nhietdo2}</span>
                    <img style={{ position: "absolute", top: 5,left:"15px" }} src={d} alt="" />
                </div>
            </div>
        </div>
    );
}

export default Item;
