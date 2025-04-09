console.log('Swagger UI Extension script carregado!');

// Este script é executado após o carregamento do Swagger UI
window.onload = function() {
  console.log('Inicializando Swagger UI com extensões...');
  
  // Inicializar o Swagger UI
  if (typeof window.initializeSwaggerUI === 'function') {
    window.initializeSwaggerUI();
  }
  
  // Aguardar o Swagger UI ser inicializado
  const interval = setInterval(() => {
    if (window.ui) {
      clearInterval(interval);
      console.log('Swagger UI inicializado, configurando interceptor...');
      
      // Interceptar todas as respostas
      const originalFetch = window.fetch;
      window.fetch = async function(...args) {
        console.log('Interceptando requisição:', args[0]);
        const response = await originalFetch.apply(this, args);
        
        // Clonar a resposta para poder ler o corpo
        const clone = response.clone();
        
        // Verificar se é uma resposta do endpoint de login
        if (args[0].includes('/login') || args[0].includes('/auth')) {
          try {
            const data = await clone.json();
            console.log('Resposta de login interceptada:', data);
            
            // Verificar diferentes formatos possíveis do token
            const token = data.accessToken || data.access_token || data.token || 
                         (data.data && data.data.accessToken) || 
                         (data.data && data.data.token);
            
            if (token) {
              console.log('Token capturado:', token);
              // Definir o token no Swagger UI
              window.ui.preauthorizeApiKey('bearerAuth', token);
              
              // Armazenar o token no localStorage para persistência
              localStorage.setItem('swagger_accessToken', token);
            } else {
              console.warn('Token não encontrado na resposta:', data);
            }
          } catch (e) {
            console.error('Erro ao processar resposta:', e);
          }
        }
        
        return response;
      };
      
      // Restaurar token do localStorage se existir
      const savedToken = localStorage.getItem('swagger_accessToken');
      if (savedToken) {
        console.log('Restaurando token salvo:', savedToken);
        window.ui.preauthorizeApiKey('bearerAuth', 'Bearer ' + savedToken);
      }
    }
  }, 100);
};
