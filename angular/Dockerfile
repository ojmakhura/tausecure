FROM nginx:1.27.2-alpine

RUN rm -rf /usr/share/nginx/html/*
COPY dist/tausecure/browser /usr/share/nginx/html

CMD ["nginx", "-g", "daemon off;"]
