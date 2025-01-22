#include <cstdlib> // for malloc and free
#include <cstring> // for memset
#include <iostream> // for logging (assuming LOG is defined)

//base64 编码转换表，共64个
static const char base64_encode_table[] = {
    'A','B','C','D','E','F','G','H','I','J',
    'K','L','M','N','O','P','Q','R','S','T',
    'U','V','W','X','Y','Z','a','b','c','d',
    'e','f','g','h','i','j','k','l','m','n',
    'o','p','q','r','s','t','u','v','w','x',
    'y','z','0','1','2','3','4','5','6','7',
    '8','9','+','/'
};

//base64 解码表
static const unsigned char base64_decode_table[] = {
    //每行16个
    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,                //1 - 16
    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,                //17 - 32
    0,0,0,0,0,0,0,0,0,0,0,62,0,0,0,63,              //33 - 48
    52,53,54,55,56,57,58,59,60,61,0,0,0,0,0,0,      //49 - 64
    0,0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,           //65 - 80
    15,16,17,18,19,20,21,22,23,24,25,0,0,0,0,0,     //81 - 96
    0,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40, //97 - 112
    41,42,43,44,45,46,47,48,49,50,51,0,0,0,0,0      //113 - 128
};

/**
 * @param str               需编码的数据
 * @param str_len           需编码的数据大小
 * @param outlen            编码后输出的数据大小
 */
extern "C" char *base64_encode(const char *str, long str_len, long *outlen)
{
    if(str == nullptr){
        LOG(ERROR) << "base64_encode input str is nullptr";
        return nullptr;
    }
    if (str_len < 0) {
        LOG(ERROR) << "base64_encode input str_len is negative";
        return nullptr;
    }

    char *res;
    long i,j;
    int num = str_len % 3;
    long len = (str_len+2) / 3 * 4;
    res=(char*)malloc(sizeof(char)*(len+1));
    if(res==nullptr){
        LOG(ERROR) << "base64_encode malloc failed!!size:"<<len;
        return nullptr;
    }
    res[len]='\0';
    *outlen = len;

    //编码，3个字节一组，若数据总长度不是3的倍数，则跳过最后的 num 个字节数据
    for(i=0, j=0; i<(str_len - num); i+=3, j+=4) {
        res[j]     = base64_encode_table[(unsigned char)str[i] >> 2];
        res[j + 1] = base64_encode_table[(((unsigned char)str[i] & 0x03) << 4) | ((unsigned char)str[i+1] >> 4)];
        res[j + 2] = base64_encode_table[(((unsigned char)str[i+1] & 0x0f) << 2) | ((unsigned char)str[i+2] >> 6)];
        res[j + 3] = base64_encode_table[(unsigned char)str[i+2] & 0x3f];
    }
    //余数为1，需补齐两个字节'='
    if(num == 1) {
        res[j]     = base64_encode_table[(unsigned char)str[str_len-1] >> 2];
        res[j + 1] = base64_encode_table[((unsigned char)str[str_len-1] & 0x03) << 4];
        res[j + 2] = '=';
        res[j + 3] = '=';
    }
    //余数为2，需补齐一个字节'='
    else if(num == 2) {
        res[j]     = base64_encode_table[(unsigned char)str[str_len-2] >> 2];
        res[j + 1] = base64_encode_table[(((unsigned char)str[str_len-2] & 0x03) << 4) | ((unsigned char)str[str_len-1] >> 4)];
        res[j + 2] = base64_encode_table[((unsigned char)str[str_len-1] & 0x0f) << 2];
        res[j + 3] = '=';
    }
    return res;
}
/*
char *base64_encodezz(const char *data, size_t input_length, long *out_len) {
    size_t output_length = 4 * ((input_length + 2) / 3);
    *out_len = output_length;
    char *encoded_data = (char*)malloc(sizeof(char)*(output_length+1));
    if (encoded_data == NULL) return NULL;
    for (size_t i = 0, j = 0; i < input_length;) {
        uint32_t octet_a = i < input_length ? (unsigned char)data[i++] : 0;
        uint32_t octet_b = i < input_length ? (unsigned char)data[i++] : 0;
        uint32_t octet_c = i < input_length ? (unsigned char)data[i++] : 0;
        uint32_t triple = (octet_a << 0x10) + (octet_b << 0x08) + octet_c;
        encoded_data[j++] = base64_encode_table[(triple >> 3 * 6) & 0x3F];
        encoded_data[j++] = base64_encode_table[(triple >> 2 * 6) & 0x3F];
        encoded_data[j++] = base64_encode_table[(triple >> 1 * 6) & 0x3F];
        encoded_data[j++] = base64_encode_table[(triple >> 0 * 6) & 0x3F];
    }
    for (size_t i = 0; i < (3 - input_length % 3) % 3; i++) {
        encoded_data[output_length - 1 - i] = '=';
    }
    encoded_data[output_length] = '\0';
    return encoded_data;
}
*/

/**
 * @param str           需解码的数据
 * @param str_len       需解码的数据大小
 * @param outlen        解码后输出的数据大小
 */
extern "C" char *base64_decode(const char *str, long str_len, long *outlen){
    if(str == nullptr){
        LOG(ERROR) << "base64_decode input str is nullptr";
        return nullptr;
    }
    if (str_len < 0) {
        LOG(ERROR) << "base64_decode input str_len is negative";
        return nullptr;
    }
    char *res;
    long len = str_len / 4 * 3;
    if(str[len-1] == '='){
        len--;
    }
    if(str[len-2] == '='){
        len--;
    }
    res=(char*)malloc(sizeof(char)*(len+1));
    if(res==nullptr){
        LOG(ERROR) << "base64_encode malloc failed!!size:"<<len;
        return nullptr;
    }
    res[len] = '\0';
    *outlen = len;
    long i,j;
    for(i=0,j=0; i<str_len; i+=4){
        res[j] = (base64_decode_table[str[i]] << 2) | (base64_decode_table[str[i+1]] >> 4);
        j++;
        if(str[i+2] != '='){
            res[j] = (base64_decode_table[str[i+1]] << 4) | (base64_decode_table[str[i+2]] >> 2);
            j++;
        }
        if(str[i+3] != '='){
            res[j] = (base64_decode_table[str[i+2]] << 6) | (base64_decode_table[str[i+3]]);
            j++;
        }
    }
    return res;
}

