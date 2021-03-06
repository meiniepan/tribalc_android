package com.gs.buluo.app.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.BillEntity;
import com.gs.buluo.app.bean.HomeMessage;
import com.gs.buluo.app.bean.HomeMessageBody;
import com.gs.buluo.app.bean.HomeMessageEnum;
import com.gs.buluo.app.bean.HomeMessageType;
import com.gs.buluo.app.bean.RentProtocol;
import com.gs.buluo.app.bean.WalletAccount;
import com.gs.buluo.app.network.DepartmentApi;
import com.gs.buluo.app.network.HomeMessagesApis;
import com.gs.buluo.app.network.MoneyApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.FresoUtils;
import com.gs.buluo.app.view.activity.BillDetailActivity;
import com.gs.buluo.app.view.activity.BoardroomRecordDetailActivity;
import com.gs.buluo.app.view.activity.CompanyManagerActivity;
import com.gs.buluo.app.view.activity.CompanyPayRentActivity;
import com.gs.buluo.app.view.activity.CreditActivity;
import com.gs.buluo.app.view.activity.DepartmentActivity;
import com.gs.buluo.app.view.activity.HousePaymentActivity;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.CommonUtils;
import com.gs.buluo.common.utils.DensityUtils;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.utils.TribeDateUtils;
import com.gs.buluo.common.widget.LoadingDialog;

import java.util.ArrayList;
import java.util.Date;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Solang on 2017/7/20.
 */

public class HomeMessageAdapter extends RecyclerView.Adapter {
    ArrayList<HomeMessage> datas;
    Activity mContext;
    String TAG = "HomeMessage";

    public HomeMessageAdapter(Activity context, ArrayList<HomeMessage> datas) {
        this.datas = datas;
        mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (datas.get(position).messageBody.homeMessageType.homeMessageTypeEnum == null) return 0;
        switch (datas.get(position).messageBody.homeMessageType.homeMessageTypeEnum) {
            case COMPANIES_ADMIN:
                return 1;
            case COMPANIES_RENT_BILL_PAYMENT:
                return 2;
            case COMPANIES_RENT_BILL_GENERATION:
                return 3;
            case CREDIT_DISABLE:
                return 4;
            case CREDIT_ENABLE:
                return 5;
            case CREDIT_BILL_GENERATION:
                return 6;
            case CREDIT_BILL_PAYMENT:
                return 7;
            case RENT_BILL_GENERATION:
                return 8;
            case RENT_BILL_PAYMENT:
                return 9;
            case RENT_CHECK_IN:
                return 10;
            case ACCOUNT_WALLET_PAYMENT:
            case WELFARE_PAYMENT:
                return 11;
            case ACCOUNT_WALLET_RECHARGE:
            case ACCOUNT_WALLET_WITHDRAW:
            case ORDER_REFUND:
            case COMPANIES_WALLET_WITHDRAW:
                return 12;
            case ACCOUNT_REGISTER:
                return 13;
            case WELFARE:
                return 14;
            case CONFERENCE_RESERVATION_SUCCESS:
            case CONFERENCE_RESERVATION_REMIND:
                return 15;
            case CONFERENCE_RESERVATION_CANCEL:
                return 16;
            default:
                return 0;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        switch (viewType) {
            case 1:
                holder = new ViewHolderCompaniesAdmin(LayoutInflater.from(mContext).inflate(R.layout.message_type_companies_admin, parent, false));
                break;
            case 2:
                holder = new ViewHolderCompaniesBillPayment(LayoutInflater.from(mContext).inflate(R.layout.message_type_companies_bill_payment, parent, false));
                break;
            case 3:
                holder = new ViewHolderCompaniesRentBillGeneration(LayoutInflater.from(mContext).inflate(R.layout.message_type_companies_rent_bill_generation, parent, false));
                break;
            case 4:
                holder = new ViewHolderCreditDisable(LayoutInflater.from(mContext).inflate(R.layout.message_type_credit_disable, parent, false));
                break;
            case 5:
                holder = new ViewHolderCreditEnable(LayoutInflater.from(mContext).inflate(R.layout.message_type_credit_enable, parent, false));
                break;
            case 6:
                holder = new ViewHolderCreditGeneration(LayoutInflater.from(mContext).inflate(R.layout.message_type_credit_generation, parent, false));
                break;
            case 7:
                holder = new ViewHolderCreditPayment(LayoutInflater.from(mContext).inflate(R.layout.message_type_credit_payment, parent, false));
                break;
            case 8:
                holder = new ViewHolderRentBillGeneration(LayoutInflater.from(mContext).inflate(R.layout.message_type_rent_bill_generation, parent, false));
                break;
            case 9:
                holder = new ViewHolderRentBillPayment(LayoutInflater.from(mContext).inflate(R.layout.message_type_rent_bill_payment, parent, false));
                break;
            case 10:
                holder = new ViewHolderRentCheckIn(LayoutInflater.from(mContext).inflate(R.layout.message_type_rent_check_in, parent, false));
                break;
            case 11:
                holder = new ViewHolderWalletPay(LayoutInflater.from(mContext).inflate(R.layout.message_type_wallet_pay, parent, false));
                break;
            case 12:
                holder = new ViewHolderWalletRecharge(LayoutInflater.from(mContext).inflate(R.layout.message_type_wallet_recharge, parent, false));
                break;
            case 13:
                holder = new ViewHolderAccountRegister(LayoutInflater.from(mContext).inflate(R.layout.message_type_account_register, parent, false));
                break;
            case 14:
                holder = new ViewHolderWelfare(LayoutInflater.from(mContext).inflate(R.layout.message_type_welfare, parent, false));
                break;
            case 15:
                holder = new ViewHolderConferenceReservation(LayoutInflater.from(mContext).inflate(R.layout.message_type_conference_reservation, parent, false));
                break;
            case 16:
                holder = new ViewHolderConferenceReservation(LayoutInflater.from(mContext).inflate(R.layout.message_type_conference_cancel, parent, false));
                break;
            default:
                holder = new ViewHolderNotExist(LayoutInflater.from(mContext).inflate(R.layout.message_type_not_exist, parent, false));
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        HomeMessageBody data = datas.get(position).messageBody;
        final HomeMessage message = datas.get(position);
        ((ViewHolderBase) holder).pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow(v, message);
            }
        });
        ((ViewHolderBase) holder).body.setText(data.body);
        ((ViewHolderBase) holder).check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doCheck(message.messageBody);
            }
        });
        String creDate = TribeDateUtils.SDF9.format(datas.get(position).createTime);
        String curDate = TribeDateUtils.SDF9.format(System.currentTimeMillis());
        if (!creDate.substring(0, 3).equals(curDate.substring(0, 3))) {
            ((ViewHolderBase) holder).date.setText(creDate);
        } else if (creDate.substring(6, 10).equals(curDate.substring(6, 10))) {
            ((ViewHolderBase) holder).date.setText(creDate.substring(11, creDate.length()));
        } else {
            ((ViewHolderBase) holder).date.setText(creDate.substring(5, creDate.length()));
        }
        if (holder instanceof ViewHolderCompaniesAdmin) {
            ((ViewHolderCompaniesAdmin) holder).desc.setText(data.description);
        } else if (holder instanceof ViewHolderCompaniesBillPayment) {
            ((ViewHolderCompaniesBillPayment) holder).desc.setText(data.description);
            ((ViewHolderCompaniesBillPayment) holder).periodicity.setText(data.periodicity + "");
            ((ViewHolderCompaniesBillPayment) holder).repaymentAmount.setText(data.repaymentAmount + "");
        } else if (holder instanceof ViewHolderRentBillGeneration) {
            ((ViewHolderRentBillGeneration) holder).desc.setText(data.description);
            ((ViewHolderRentBillGeneration) holder).periodicity.setText(data.periodicity + "");
            ((ViewHolderRentBillGeneration) holder).repaymentAmount.setText(data.repaymentAmount + "");
            ((ViewHolderRentBillGeneration) holder).repaymentTime.setText(TribeDateUtils.SDF5.format(data.repaymentTime));
        } else if (holder instanceof ViewHolderCreditDisable) {
            ((ViewHolderCreditDisable) holder).desc.setText(data.description);
            ((ViewHolderCreditDisable) holder).repaymentAmount.setText(data.repaymentAmount + "");
        } else if (holder instanceof ViewHolderCreditEnable) {
            ((ViewHolderCreditEnable) holder).desc.setText(data.description);
        } else if (holder instanceof ViewHolderCreditGeneration) {
            ((ViewHolderCreditGeneration) holder).repaymentAmount.setText(data.repaymentAmount + "");
            ((ViewHolderCreditGeneration) holder).repaymentTime.setText(TribeDateUtils.SDF5.format(data.repaymentTime));
        } else if (holder instanceof ViewHolderCreditPayment) {
            ((ViewHolderCreditPayment) holder).repaymentAmount.setText(data.repaymentAmount + "");
        } else if (holder instanceof ViewHolderRentBillPayment) {
            ((ViewHolderRentBillPayment) holder).desc.setText(data.description);
            ((ViewHolderRentBillPayment) holder).periodicity.setText(data.periodicity + "");
            ((ViewHolderRentBillPayment) holder).repaymentAmount.setText(data.repaymentAmount + "");
        } else if (holder instanceof ViewHolderRentCheckIn) {
        } else if (holder instanceof ViewHolderWalletPay) {
            ((ViewHolderWalletPay) holder).desc.setText(data.description);
            FresoUtils.loadImage("oss://" + data.avatar + "/icon.jpg", ((ViewHolderWalletPay) holder).avatar);
        } else if (holder instanceof ViewHolderWalletRecharge) {
            ((ViewHolderWalletRecharge) holder).desc.setText(data.description);
            FresoUtils.loadImage("oss://" + data.avatar + "/icon.jpg", ((ViewHolderWalletRecharge) holder).avatar);
        } else if (holder instanceof ViewHolderWelfare) {
            ((ViewHolderWelfare) holder).desc.setText(data.description);
            ((ViewHolderWelfare) holder).remark.setText(data.remark);
        } else if (holder instanceof ViewHolderConferenceReservation) {
            ((ViewHolderConferenceReservation) holder).desc.setText(data.description);
            ((ViewHolderConferenceReservation) holder).remark.setText(data.remark);
            ((ViewHolderConferenceReservation) holder).date.setText(TribeDateUtils.dateFormat(new Date(data.applicationTime)));
        } else if (holder instanceof ViewHolderNotExist) {
            ((ViewHolderNotExist) holder).body.setText(mContext.getString(R.string.message_type_not_exist));
        }
    }

    private void doCheck(HomeMessageBody homeMessageBody) {
        Intent intent = new Intent();
        HomeMessageType homeMessageType = homeMessageBody.homeMessageType;
        String referenceId = homeMessageBody.referenceId;
        switch (homeMessageType.homeMessageTypeEnum) {
            case ACCOUNT_WALLET_PAYMENT:
            case ACCOUNT_WALLET_RECHARGE:
            case CREDIT_BILL_PAYMENT:
            case WELFARE:
            case RENT_BILL_PAYMENT:
            case COMPANIES_WALLET_WITHDRAW:
            case ACCOUNT_WALLET_WITHDRAW:
            case COMPANIES_RENT_BILL_PAYMENT:
            case ORDER_REFUND:
                getBillDetail(intent, referenceId);
                break;
            case WELFARE_PAYMENT:
                intent.setClass(mContext, BillDetailActivity.class);
                intent.putExtra(Constant.WARFARE, referenceId);
                mContext.startActivity(intent);
                break;
            case CREDIT_DISABLE:
            case CREDIT_ENABLE:
            case CREDIT_BILL_GENERATION:
                getWalletInfo(intent);
                break;
            case RENT_CHECK_IN:
                intent.setClass(mContext, DepartmentActivity.class);
                mContext.startActivity(intent);
                break;
            case COMPANIES_ADMIN:
                intent.setClass(mContext, CompanyManagerActivity.class);
                mContext.startActivity(intent);
                break;
            case COMPANIES_RENT_BILL_GENERATION:
                intent.setClass(mContext, CompanyPayRentActivity.class);
                mContext.startActivity(intent);
                break;
            case RENT_BILL_GENERATION:
                getApartment(intent, referenceId);
                break;
            case ACCOUNT_REGISTER:
                break;
            case CONFERENCE_RESERVATION_SUCCESS:
            case CONFERENCE_RESERVATION_REMIND:
            case CONFERENCE_RESERVATION_CANCEL:
                intent.setClass(mContext, BoardroomRecordDetailActivity.class);
                intent.putExtra(Constant.BOARD_RESERVE_ID, referenceId);
                mContext.startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void getBillDetail(final Intent intent, String referenceId) {
        String id = TribeApplication.getInstance().getUserInfo().getId();
        LoadingDialog.getInstance().show(mContext, "加载中", true);
        TribeRetrofit.getInstance().createApi(MoneyApis.class).
                getBillDetail(id, referenceId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<BillEntity>>() {
                               @Override
                               public void onNext(BaseResponse<BillEntity> response) {
                                   intent.setClass(mContext, BillDetailActivity.class);
                                   intent.putExtra(Constant.BILL, response.data);
                                   mContext.startActivity(intent);
                               }

                               @Override
                               public void onFail(ApiException e) {
                                   ToastUtils.ToastMessage(mContext, R.string.connect_fail);
                               }
                           }
                );
    }

    private void getApartment(final Intent intent, String referenceId) {
        String id = TribeApplication.getInstance().getUserInfo().getId();
        LoadingDialog.getInstance().show(mContext, "加载中", true);
        TribeRetrofit.getInstance().createApi(DepartmentApi.class).
                getApartment(id, referenceId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<RentProtocol>>() {
                               @Override
                               public void onNext(BaseResponse<RentProtocol> response) {
                                   intent.setClass(mContext, HousePaymentActivity.class);
                                   intent.putExtra(Constant.RENT_PROTOCOL_ID, response.data.id);
                                   intent.putExtra(Constant.RENT_APARTMENT_CODE, response.data.sourceNum);
                                   intent.putExtra(Constant.RENT_APARTMENT_NAME, response.data.sourceName);
                                   mContext.startActivity(intent);
                               }
                           }
                );
    }

    private void getWalletInfo(final Intent intent) {
        String id = TribeApplication.getInstance().getUserInfo().getId();
        LoadingDialog.getInstance().show(mContext, "加载中", true);
        TribeRetrofit.getInstance().createApi(MoneyApis.class).
                getWallet(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<WalletAccount>>() {
                               @Override
                               public void onNext(BaseResponse<WalletAccount> response) {
                                   intent.setClass(mContext, CreditActivity.class);
                                   intent.putExtra(Constant.WALLET, response.data);
                                   mContext.startActivity(intent);
                               }
                           }
                );
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class
    ViewHolderBase extends RecyclerView.ViewHolder {
        public TextView body;
        public TextView pop;
        public TextView date;
        public TextView check;

        public ViewHolderBase(View itemView) {
            super(itemView);
            body = (TextView) itemView.findViewById(R.id.body);
            pop = (TextView) itemView.findViewById(R.id.tv_pop);
            date = (TextView) itemView.findViewById(R.id.date);
            check = (TextView) itemView.findViewById(R.id.tv_check);
        }
    }

    public class ViewHolderAccountRegister extends ViewHolderBase {
        public TextView body;
        public TextView desc;
        public TextView pop;

        public ViewHolderAccountRegister(View itemView) {
            super(itemView);
            body = (TextView) itemView.findViewById(R.id.body);
            desc = (TextView) itemView.findViewById(R.id.desc);
            pop = (TextView) itemView.findViewById(R.id.tv_pop);

        }
    }

    public class ViewHolderCompaniesAdmin extends ViewHolderBase {
        public TextView body;
        public TextView desc;
        public TextView repaymentTime;
        public TextView repaymentAmount;
        public TextView periodicity;
        public TextView pop;

        public ViewHolderCompaniesAdmin(View itemView) {
            super(itemView);
            body = (TextView) itemView.findViewById(R.id.body);
            desc = (TextView) itemView.findViewById(R.id.desc);
            repaymentTime = (TextView) itemView.findViewById(R.id.repayment_time);
            repaymentAmount = (TextView) itemView.findViewById(R.id.repayment_amount);
            periodicity = (TextView) itemView.findViewById(R.id.periodicity);
            pop = (TextView) itemView.findViewById(R.id.tv_pop);

        }
    }

    public class ViewHolderCompaniesBillPayment extends ViewHolderBase {
        public SimpleDraweeView avatar;
        public TextView body;
        public TextView desc;
        public TextView repaymentTime;
        public TextView repaymentAmount;
        public TextView periodicity;
        public TextView pop;

        public ViewHolderCompaniesBillPayment(View itemView) {
            super(itemView);
            body = (TextView) itemView.findViewById(R.id.body);
            desc = (TextView) itemView.findViewById(R.id.desc);
            repaymentAmount = (TextView) itemView.findViewById(R.id.repayment_amount);
            periodicity = (TextView) itemView.findViewById(R.id.periodicity);
            pop = (TextView) itemView.findViewById(R.id.tv_pop);

        }
    }

    public class ViewHolderCompaniesRentBillGeneration extends ViewHolderBase {
        public SimpleDraweeView avatar;
        public TextView body;
        public TextView desc;
        public TextView repaymentTime;
        public TextView repaymentAmount;
        public TextView periodicity;
        public TextView pop;

        public ViewHolderCompaniesRentBillGeneration(View itemView) {
            super(itemView);
            body = (TextView) itemView.findViewById(R.id.body);
            desc = (TextView) itemView.findViewById(R.id.desc);
            repaymentTime = (TextView) itemView.findViewById(R.id.repayment_time);
            repaymentAmount = (TextView) itemView.findViewById(R.id.repayment_amount);
            periodicity = (TextView) itemView.findViewById(R.id.periodicity);
            pop = (TextView) itemView.findViewById(R.id.tv_pop);

        }
    }

    public class ViewHolderCreditDisable extends ViewHolderBase {
        public SimpleDraweeView avatar;
        public TextView body;
        public TextView desc;
        public TextView repaymentTime;
        public TextView repaymentAmount;
        public TextView periodicity;
        public TextView pop;

        public ViewHolderCreditDisable(View itemView) {
            super(itemView);
            body = (TextView) itemView.findViewById(R.id.body);
            desc = (TextView) itemView.findViewById(R.id.desc);
            repaymentAmount = (TextView) itemView.findViewById(R.id.repayment_amount);
            pop = (TextView) itemView.findViewById(R.id.tv_pop);

        }
    }

    public class ViewHolderCreditEnable extends ViewHolderBase {
        public SimpleDraweeView avatar;
        public TextView body;
        public TextView desc;
        public TextView repaymentTime;
        public TextView repaymentAmount;
        public TextView periodicity;
        public TextView pop;

        public ViewHolderCreditEnable(View itemView) {
            super(itemView);
            body = (TextView) itemView.findViewById(R.id.body);
            desc = (TextView) itemView.findViewById(R.id.desc);
            pop = (TextView) itemView.findViewById(R.id.tv_pop);

        }
    }

    public class ViewHolderCreditGeneration extends ViewHolderBase {
        public SimpleDraweeView avatar;
        public TextView body;
        public TextView desc;
        public TextView repaymentTime;
        public TextView repaymentAmount;
        public TextView periodicity;
        public TextView pop;

        public ViewHolderCreditGeneration(View itemView) {
            super(itemView);
            body = (TextView) itemView.findViewById(R.id.body);
            repaymentTime = (TextView) itemView.findViewById(R.id.repayment_time);
            repaymentAmount = (TextView) itemView.findViewById(R.id.repayment_amount);
            pop = (TextView) itemView.findViewById(R.id.tv_pop);

        }
    }

    public class ViewHolderCreditPayment extends ViewHolderBase {
        public SimpleDraweeView avatar;
        public TextView body;
        public TextView desc;
        public TextView repaymentTime;
        public TextView repaymentAmount;
        public TextView periodicity;
        public TextView pop;

        public ViewHolderCreditPayment(View itemView) {
            super(itemView);
            body = (TextView) itemView.findViewById(R.id.body);
            repaymentAmount = (TextView) itemView.findViewById(R.id.repayment_amount);
            pop = (TextView) itemView.findViewById(R.id.tv_pop);

        }
    }

    public class ViewHolderRentBillGeneration extends ViewHolderBase {
        public SimpleDraweeView avatar;
        public TextView body;
        public TextView desc;
        public TextView repaymentTime;
        public TextView repaymentAmount;
        public TextView periodicity;
        public TextView pop;

        public ViewHolderRentBillGeneration(View itemView) {
            super(itemView);
            body = (TextView) itemView.findViewById(R.id.body);
            desc = (TextView) itemView.findViewById(R.id.desc);
            repaymentTime = (TextView) itemView.findViewById(R.id.repayment_time);
            repaymentAmount = (TextView) itemView.findViewById(R.id.repayment_amount);
            periodicity = (TextView) itemView.findViewById(R.id.periodicity);
            pop = (TextView) itemView.findViewById(R.id.tv_pop);

        }
    }

    public class ViewHolderRentBillPayment extends ViewHolderBase {
        public TextView body;
        public TextView desc;
        public TextView repaymentAmount;
        public TextView periodicity;
        public TextView pop;

        public ViewHolderRentBillPayment(View itemView) {
            super(itemView);
            body = (TextView) itemView.findViewById(R.id.body);
            desc = (TextView) itemView.findViewById(R.id.desc);
            repaymentAmount = (TextView) itemView.findViewById(R.id.repayment_amount);
            periodicity = (TextView) itemView.findViewById(R.id.periodicity);
            pop = (TextView) itemView.findViewById(R.id.tv_pop);

        }
    }

    public class ViewHolderRentCheckIn extends ViewHolderBase {
        public TextView body;
        public TextView desc;
        public TextView pop;

        public ViewHolderRentCheckIn(View itemView) {
            super(itemView);
            body = (TextView) itemView.findViewById(R.id.body);
            pop = (TextView) itemView.findViewById(R.id.tv_pop);


        }
    }

    public class ViewHolderWalletPay extends ViewHolderBase {
        public SimpleDraweeView avatar;
        public TextView body;
        public TextView desc;
        public TextView pop;

        public ViewHolderWalletPay(View itemView) {
            super(itemView);
            body = (TextView) itemView.findViewById(R.id.body);
            desc = (TextView) itemView.findViewById(R.id.desc);
            avatar = (SimpleDraweeView) itemView.findViewById(R.id.avatar);
            pop = (TextView) itemView.findViewById(R.id.tv_pop);

        }
    }

    public class ViewHolderNotExist extends ViewHolderBase {
        public TextView body;

        public ViewHolderNotExist(View itemView) {
            super(itemView);
            body = (TextView) itemView.findViewById(R.id.body);
        }
    }

    public class ViewHolderWalletRecharge extends ViewHolderBase {
        public SimpleDraweeView avatar;
        public TextView body;
        public TextView desc;
        public TextView pop;

        public ViewHolderWalletRecharge(View itemView) {
            super(itemView);
            body = (TextView) itemView.findViewById(R.id.body);
            desc = (TextView) itemView.findViewById(R.id.desc);
            avatar = (SimpleDraweeView) itemView.findViewById(R.id.avatar);
            pop = (TextView) itemView.findViewById(R.id.tv_pop);

        }
    }

    public class ViewHolderWelfare extends ViewHolderBase {
        public TextView body;
        public TextView desc;
        public TextView remark;

        public ViewHolderWelfare(View itemView) {
            super(itemView);
            body = (TextView) itemView.findViewById(R.id.body);
            desc = (TextView) itemView.findViewById(R.id.desc);
            remark = (TextView) itemView.findViewById(R.id.remark);

        }
    }

    public class ViewHolderConferenceReservation extends ViewHolderBase {
        public TextView desc;
        public TextView date;
        public TextView remark;

        public ViewHolderConferenceReservation(View itemView) {
            super(itemView);
            desc = itemView.findViewById(R.id.desc);
            date = itemView.findViewById(R.id.periodicity);
            remark = itemView.findViewById(R.id.conference_remark);
        }
    }

    private void showPopupWindow(View view, final HomeMessage data) {

        View contentView = LayoutInflater.from(mContext).inflate(
                R.layout.main_frag_list_button, null);

        final PopupWindow popupWindow = new PopupWindow(contentView,
                CommonUtils.getScreenWidth(mContext) - 20, DensityUtils.dip2px(mContext, 80), true);
        contentView.findViewById(R.id.tv_main_frag_ignore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = datas.indexOf(data);
                datas.remove(data);
                ignoreOnHttp(data.id);
                notifyItemRemoved(pos + 2);
                popupWindow.dismiss();
            }
        });
        contentView.findViewById(R.id.tv_main_frag_refuse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = datas.indexOf(data);
                datas.remove(data);
                refuseOnHttp(data.messageBody.homeMessageType.homeMessageTypeEnum);
                notifyItemRemoved(pos + 2);
                popupWindow.dismiss();
            }
        });
        popupWindow.setTouchable(true);
        popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
        popupWindow.setOnDismissListener(new HomeMessageAdapter.popOnDismissListener());
        CommonUtils.backgroundAlpha(mContext, 0.61f);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        int windowPos[] = calculatePopWindowPos(view, contentView);
//        int xOff = 20;// 可以自己调整偏移
//        windowPos[0] -= xOff;
        popupWindow.showAtLocation(view, Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, windowPos[1]);
    }

    private void refuseOnHttp(HomeMessageEnum homeMessageTypeEnum) {
        TribeRetrofit.getInstance().createApi(HomeMessagesApis.class).refuseMessage(TribeApplication.getInstance().getUserInfo().getId(), homeMessageTypeEnum)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse response) {
                        Log.e(TAG, "refuseSuccess");
                    }

                    @Override
                    public void onFail(ApiException e) {
                        super.onFail(e);
                    }
                });
    }

    private void ignoreOnHttp(String id) {
        TribeRetrofit.getInstance().createApi(HomeMessagesApis.class).ignoreMessage(TribeApplication.getInstance().getUserInfo().getId(), id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse response) {
                        Log.e(TAG, "ignoreSuccess");
                    }

                    @Override
                    public void onFail(ApiException e) {
                        super.onFail(e);
                    }
                });
    }

    /**
     * 计算出来的位置，y方向就在anchorView的上面和下面对齐显示，x方向就是与屏幕右边对齐显示
     * 如果anchorView的位置有变化，就可以适当自己额外加入偏移来修正
     *
     * @param anchorView  呼出window的view
     * @param contentView window的内容布局
     * @return window显示的左上角的xOff, yOff坐标
     */
    private static int[] calculatePopWindowPos(final View anchorView, final View contentView) {
        final int windowPos[] = new int[2];
        final int anchorLoc[] = new int[2];
        // 获取锚点View在屏幕上的左上角坐标位置
        anchorView.getLocationOnScreen(anchorLoc);
        final int anchorHeight = anchorView.getHeight();
        // 获取屏幕的高宽
        final int screenHeight = CommonUtils.getScreenHeight(anchorView.getContext());
        final int screenWidth = CommonUtils.getScreenWidth(anchorView.getContext());
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        // 计算contentView的高宽
        final int windowHeight = contentView.getMeasuredHeight();
        final int windowWidth = contentView.getMeasuredWidth();
        // 判断需要向上弹出还是向下弹出显示
        final boolean isNeedShowUp = (screenHeight - anchorLoc[1] - anchorHeight < (windowHeight + DensityUtils.dip2px(contentView.getContext(), 50)));
        if (isNeedShowUp) {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] - windowHeight;
            contentView.setBackground(contentView.getContext().getResources().getDrawable(
                    R.mipmap.pop_up));
        } else {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] + anchorHeight;
            contentView.setBackground(contentView.getContext().getResources().getDrawable(
                    R.mipmap.pop_down));
        }
        return windowPos;
    }

    class popOnDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            //Log.v("List_noteTypeActivity:", "我是关闭事件");
            CommonUtils.backgroundAlpha(mContext, 1f);
        }

    }
}
