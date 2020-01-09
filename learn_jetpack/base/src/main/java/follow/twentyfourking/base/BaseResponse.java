package follow.twentyfourking.base;

/**
 * 接口请求基本数据解析
 * @param <T>
 */
public class BaseResponse<T> {
    private String errorCode;
    private String errorMsg;

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}

