package com.otitan.util;

import java.io.IOException;
import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

public class Communication {
	
	private static Communication mCommunication;
	
	private Communication(){
	}
	
	public static Communication getInstance() {
		if (mCommunication == null) {
			mCommunication = new Communication();
		}
		
		return mCommunication;
	}
	
	private String strContent = null;
	
	/**
	 * 
	 *  �������� : connecting
	 *  �������� :  
	 *  ����������ֵ˵����
	 *  	@param nameSpace 	���ƿռ�
	 *  	@param methodName 	������
	 *  	@param parameters 	��������
	 *  	@param WSDLurl 		����WSDL��ַ
	 *  	@return
	 *
	 *  �޸ļ�¼��
	 *  	���ڣ�2013-3-11 ����03:57:05	�޸��ˣ�adminstrator
	 *  	����	����������˵�webServices��������
	 *
	 */
	public Object connecting(String nameSpace,String methodName,ArrayList<Object> parameters,String WSDLurl) {
		
		/**
		 * 1��ʵ���� SoapObject ����
		 * ָ�� WebService �������ռ�͵��õķ�������
		 * SoapObject ��ĵ� 1 ��������ʾ WebService �������ռ䣬���Դ� WSDL �ĵ����ҵ� WebService �������ռ䡣
		 * �� 2 ��������ʾҪ���õ� WebService ������
		 */
		SoapObject request = new SoapObject(nameSpace, methodName);
		/**
		 * 2�����õ��÷����Ĳ���ֵ����һ���ǿ�ѡ�ģ��������û�в���������ʡ����һ����
		 * Ҫע����ǣ�addProperty �����ĵ� 1 ��������Ȼ��ʾ���÷����Ĳ�������
		 * �˴��Ĳ������Ķ��岻�Ƿ���˵� WebService ���еķ����������������� Jax-ws �ʱ�Զ����ɵ�  WEB-INF/wsdl/*Service_schema*.xsd �ļ������õķ�������
		 * <xs:element minOccurs="0" name="arg0" type="xs:string"/>��Ҳ���Ǻ�����Ϊ arg0
		 */
		if (parameters != null) {
			for (int i = 0; i < parameters.size(); i++) {
				request.addProperty("arg" + i, parameters.get(i).toString());
			}
		}
		
		/**
		 * 3�����ɵ��� WebService ������ SOAP ������Ϣ������Ϣ�� SoapSerializationEnvelope ��������
		 */
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		/**
		 * ���� SoapSerializationEnvelope ����ʱ��Ҫͨ�� SoapSerializationEnvelope ��Ĺ��췽������ SOAP Э ��İ汾�š�
		 * �ð汾����Ҫ���ݷ���� WebService �İ汾�����á��ڴ��� SoapSerializationEnvelope �����
		 * ��Ҫ�������� SoapSerializationEnvelope ��� bodyOut���ԣ������Ե�ֵ�����ڵ� 1 �������� SoapObject ����
		 */
		envelope.bodyOut = request;
		
		/**
		 * 4������ HttpTransportSE ����ͨ�� HttpTransportSE ��Ĺ��췽������ָ�� WebService �� WSDL �ĵ��� URL
		 */
		HttpTransportSE ht = new HttpTransportSE(WSDLurl);
		try
		{
			/**
			 * 5��ʹ�� call �������� WebService ������
			 * call �����ĵ� 1 ������һ��Ϊ null���� 2 �����������ڵ� 3 �������� SoapSerializationEnvelope ����
			 */
			ht.call(null, envelope);
			/**
			 * 6��ʹ�� getResponse ������� WebService �����ķ��ؽ��
			 */
			System.out.println("envelope.getResponse()=" + envelope.getResponse().getClass());
			
			
			if(envelope.getResponse() instanceof SoapPrimitive)
			{
				strContent = ((SoapPrimitive)envelope.getResponse()).toString();
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (XmlPullParserException e)
		{
			e.printStackTrace();
		}
		
		return strContent;
	}
}
